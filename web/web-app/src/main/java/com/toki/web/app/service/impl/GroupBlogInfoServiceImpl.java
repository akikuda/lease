package com.toki.web.app.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.toki.common.exception.LeaseException;
import com.toki.common.utils.LoginUser;
import com.toki.common.utils.LoginUserHolder;
import com.toki.model.entity.GroupBlogInfo;
import com.toki.model.entity.GroupFollow;
import com.toki.model.entity.UserInfo;
import com.toki.web.app.mapper.GroupBlogInfoMapper;
import com.toki.web.app.service.GroupBlogInfoService;
import com.toki.web.app.service.GroupFollowService;
import com.toki.web.app.service.UserInfoService;
import com.toki.web.app.vo.group.GroupBlogVo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.toki.common.constant.RedisConstant.BLOG_LIKED_KEY;
import static com.toki.common.constant.RedisConstant.FEED_KEY;
import static com.toki.common.result.ResultCodeEnum.BLOG_SAVE_ERROR;

/**
 * @author toki
 */
@Service
@RequiredArgsConstructor
public class GroupBlogInfoServiceImpl extends ServiceImpl<GroupBlogInfoMapper, GroupBlogInfo>
        implements GroupBlogInfoService {

    private final GroupBlogInfoMapper groupBlogInfoMapper;
    private final GroupFollowService groupFollowService;
    private final UserInfoService userInfoService;

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean saveBlog(GroupBlogVo blogVo) {
        // 设置用户id
        final Long userId = LoginUserHolder.getLoginUser().getUserId();
        blogVo.setUserId(userId);
        // 设置公寓id，用来关联发圈时指定公寓，还没实现，暂时写死
        blogVo.setApartmentId(9L);

        // 发布博文
        final boolean isSuccess = save(blogVo);
        if (!isSuccess) {
            throw new LeaseException(BLOG_SAVE_ERROR);
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
    public IPage<GroupBlogVo> pageItem(Page<GroupBlogVo> page) {
        final IPage<GroupBlogVo> blogVoPage = groupBlogInfoMapper.pageItem(page);
        // 获取当页数据
        final List<GroupBlogVo> records = blogVoPage.getRecords();
        // 设置博文相关信息和点赞状态
        records.forEach(blogVo -> {
            this.queryUserIdByBlog(blogVo);
            this.isBlogLiked(blogVo);
        });
        // 设置当前页数据
        blogVoPage.setRecords(records);

        return blogVoPage;
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
     * 查询blog的作者，并将作者信息设置到blog中
     **/
    private void queryUserIdByBlog(GroupBlogVo blogVo) {
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




