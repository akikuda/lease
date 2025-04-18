package com.toki.web.app.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.toki.common.exception.LeaseException;
import com.toki.common.utils.LoginUser;
import com.toki.common.utils.LoginUserHolder;
import com.toki.model.entity.*;
import com.toki.model.enums.ItemType;
import com.toki.web.app.mapper.GroupBlogCommentsMapper;
import com.toki.web.app.mapper.GroupBlogInfoMapper;
import com.toki.web.app.service.*;
import com.toki.web.app.vo.graph.GraphVo;
import com.toki.web.app.vo.group.CommentItemVo;
import com.toki.web.app.vo.group.CommentSubmitVo;
import com.toki.web.app.vo.group.GroupBlogVo;
import com.toki.web.app.vo.user.UserInfoVo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.toki.common.constant.RedisConstant.BLOG_LIKED_KEY;
import static com.toki.common.constant.RedisConstant.FEED_KEY;
import static com.toki.common.result.ResultCodeEnum.ADMIN_LOGIN_AUTH;
import static com.toki.common.result.ResultCodeEnum.BLOG_SAVE_ERROR;

/**
 * @author toki
 */
@Service
@RequiredArgsConstructor
//@Slf4j
public class GroupBlogInfoServiceImpl extends ServiceImpl<GroupBlogInfoMapper, GroupBlogInfo>
        implements GroupBlogInfoService {

    private final GroupBlogInfoMapper groupBlogInfoMapper;
    private final GroupBlogCommentsMapper commentsMapper;

    private final GroupFollowService groupFollowService;
    private final UserInfoService userInfoService;
    private final GraphInfoService graphInfoService;

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean saveBlog(GroupBlogVo blogVo) {
        // 设置用户id
        final Long userId = LoginUserHolder.getLoginUser().getUserId();
        blogVo.setUserId(userId);

        // 发布博文
        final boolean isSuccess = save(blogVo);
        if (!isSuccess) {
            throw new LeaseException(BLOG_SAVE_ERROR);
        }

        // 设置博文图片
        final List<GraphVo> graphVoList = blogVo.getGraphVoList();
        if (!CollUtil.isEmpty(graphVoList)) {
            final ArrayList<GraphInfo> graphInfoList = new ArrayList<>();
            // 遍历VO
            for (GraphVo graphVo : graphVoList) {
                // VO转Entity
                GraphInfo graphInfo = CglibUtil.copy(graphVo, GraphInfo.class);
                graphInfo.setItemType(ItemType.GROUP);
                graphInfo.setItemId(blogVo.getId());
                // 加入列表
                graphInfoList.add(graphInfo);
            }
            // 批量插入
            graphInfoService.saveBatch(graphInfoList);
//            log.info("图集存入数据库成功 图集列表：{}", graphInfoList);
        }

        // 推送给粉丝
        final List<GroupFollow> fans = groupFollowService.query().eq("follow_user_id", userId).list();
        // 推送博文ID到粉丝
        for (GroupFollow fan : fans) {
            String key = FEED_KEY + fan.getUserId();
            // 推送,key为粉丝id，value为笔记id，score为时间戳用于排序
            stringRedisTemplate.opsForZSet().add(key, blogVo.getId().toString(), System.currentTimeMillis());
        }

        return true;
    }

    @Override
    public IPage<GroupBlogVo> pageItem(Page<GroupBlogVo> page, Long apartmentId) {

        // 这里已经查询了图片列表等，还缺少用户信息(id、头像、昵称)、点赞状态、发布时间
        final IPage<GroupBlogVo> blogVoPage = groupBlogInfoMapper.pageItem(page, apartmentId);

        // 遍历当前页数据，查询并设置缺少的信息
        // 1. 获取当页数据
        final List<GroupBlogVo> records = blogVoPage.getRecords();
        // 2. 遍历当前页数据，设置作者相关信息、博文点赞状态
        records.forEach(blogVo -> {
            this.queryUserIdByBlog(blogVo);
            this.isBlogLiked(blogVo);
        });
        // 3. 设置当前页数据
        blogVoPage.setRecords(records);

        return blogVoPage;
    }

    @Override
    public boolean removeBlogById(Long id) {

        // 先删除redis中的点赞数据
        final String key = BLOG_LIKED_KEY + id;
        stringRedisTemplate.delete(key);
        // 删除相应图片
        graphInfoService.remove(
                new LambdaQueryWrapper<>(GraphInfo.class)
                        .eq(GraphInfo::getItemType, ItemType.GROUP)
                        .eq(GraphInfo::getItemId, id)
        );
        // 删除相应评论
        commentsMapper.delete(
                new LambdaQueryWrapper<>(GroupBlogComments.class)
                        .eq(GroupBlogComments::getBlogId, id)
        );

        return removeById(id);
    }

    /**
     * 点赞或取消点赞笔记,配合redis的ZSet集合实现
     **/
    @Override
    public boolean likeBlog(Long id) {
        final Long userId = LoginUserHolder.getLoginUser().getUserId();
        // 先判断当前用户是否已经点过赞
        final String key = BLOG_LIKED_KEY + id;
        final Double score = stringRedisTemplate.opsForZSet().score(key, userId.toString());
        if (score == null) {
            // 未点过赞 数据库点赞数加1,然后添加到redis的ZSet集合
            final boolean isSuccess = update().setSql("liked = liked + 1").eq("id", id).update();
            if (isSuccess) {
                // 点赞成功，添加到redis的ZSet集合,用时间戳作为分数用于排序，格式：blogId:userId:timestamp
                stringRedisTemplate.opsForZSet().add(key, userId.toString(), System.currentTimeMillis());
            }
        } else {
            // 已经点过赞 数据库点赞数减1,然后在redis的ZSet集合内删除掉对应userId
            final boolean isSuccess = update().setSql("liked = liked - 1").eq("id", id).update();
            if (isSuccess) {
                // 取消点赞，在redis的ZSet集合内删除掉
                stringRedisTemplate.opsForZSet().remove(key, userId.toString());
            }
        }
        return score != null;
    }

    /**
     * 根据博文ID查询博文点赞的前5个用户
     **/
    @Override
    public List<UserInfoVo> queryBlogLikes(Long id) {
        final String key = BLOG_LIKED_KEY + id;
        // 查询top5的点赞用户 Zrange key 0 4
        final Set<String> top5 = stringRedisTemplate.opsForZSet().range(key, 0, 4);
        if (top5 == null || top5.isEmpty()) {
            // 没有点赞用户,返回空列表
            return Collections.emptyList();
        }
        // 解析出其中的用户id,利用Stream：String转Long,Set转List
        final List<Long> ids = top5.stream().map(Long::valueOf).collect(Collectors.toList());
        final String idStr = StrUtil.join(",", ids);
        // 根据用户id查询用户信息,并转为VO，由于mp用的是in()，所以需要自定义sql，保证排序
        return userInfoService.query()
                // 自定义sql，保证排序:where id in (ids) order by field(id,ids)
                .in("id", ids).last("order by field(id," + idStr + ")").list()
                // 转为VO
                .stream()
                .map(user -> CglibUtil.copy(user, UserInfoVo.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean saveComment(CommentSubmitVo comment) {
        // 提交评论的是当前用户，将VO转成Entries，设置属性，然后存入数据库,评论数+1即可
        final Long userId = LoginUserHolder.getLoginUser().getUserId();

        // 未登录则不能评论
        if (userId == null) {
            throw new LeaseException(ADMIN_LOGIN_AUTH);
        }

        final GroupBlogComments saveComment = CglibUtil.copy(comment, GroupBlogComments.class);
        saveComment.setUserId(userId);

        final boolean isSuccess = commentsMapper.insert(saveComment) > 0;
        if (isSuccess) {
            // 增加评论数
            update().setSql("comments = comments + 1").
                    eq("id", comment.getBlogId())
                    .update();
        }
        return isSuccess;
    }

    @Override
    public List<CommentItemVo> queryBlogCommentListById(Long id) {
        return commentsMapper.queryBlogCommentListById(id);
    }


    /**
     * 查询当前用户对blog的点赞状态
     **/
    private void isBlogLiked(GroupBlogVo blogVo) {
        final LoginUser loginUser = LoginUserHolder.getLoginUser();
        if (loginUser == null) {
            // 未登录用户无需查询点赞状态
            return;
        }
        final Long userId = loginUser.getUserId();
        final String key = BLOG_LIKED_KEY + blogVo.getId();

        // Zset,有点赞会有分数
        final Double score = stringRedisTemplate.opsForZSet().score(key, userId.toString());
        blogVo.setIsLike(score != null);
    }

    /**
     * 查询blog的作者，并设置blogVo的相关信息
     **/
    private void queryUserIdByBlog(GroupBlogVo blogVo) {

        //todo 这里若是同一个用户，可以直接从缓存中获取，这里暂时先不实现

        // 设置作者头像、昵称
        final Long userId = blogVo.getUserId();
        final UserInfo userInfo = userInfoService.getById(userId);
        blogVo.setAvatarUrl(userInfo.getAvatarUrl());
        blogVo.setName(userInfo.getNickname());
        // 设置发布时间 格式化
        Date publishTime = blogVo.getCreateTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        blogVo.setPublishTime(dateFormat.format(publishTime));
    }
}




