package com.toki.web.app.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.toki.common.utils.LoginUserHolder;
import com.toki.model.entity.GroupFollow;
import com.toki.model.entity.UserInfo;
import com.toki.web.app.mapper.GroupFollowMapper;
import com.toki.web.app.service.GroupFollowService;
import com.toki.web.app.service.UserInfoService;
import com.toki.web.app.vo.user.UserInfoVo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.toki.common.constant.RedisConstant.FOLLOWS_KEY;

/**
 * @author toki
 */
@Service
@RequiredArgsConstructor
public class GroupFollowServiceImpl extends ServiceImpl<GroupFollowMapper, GroupFollow>
        implements GroupFollowService {

    private final StringRedisTemplate stringRedisTemplate;
    private final UserInfoService userInfoService;

    @Override
    public boolean follow(Long followUserId, Boolean isFollow) {
        // 获取当前用户Id
        final Long userId = LoginUserHolder.getLoginUser().getUserId();
        String key = FOLLOWS_KEY + userId;
        if (Boolean.TRUE.equals(isFollow)) {
            // 关注
            final GroupFollow follow = new GroupFollow();
            follow.setUserId(userId);
            follow.setFollowUserId(followUserId);
            this.save(follow);
            stringRedisTemplate.opsForSet().add(key, followUserId.toString());
        } else {
            // 取消关注
            remove(new LambdaQueryWrapper<GroupFollow>()
                    .eq(GroupFollow::getUserId, userId)
                    .eq(GroupFollow::getFollowUserId, followUserId)
                    .eq(GroupFollow::getIsDeleted, 0));
            stringRedisTemplate.opsForSet().remove(key, followUserId.toString());
        }
        return Boolean.TRUE.equals(isFollow);
    }

    @Override
    public boolean isFollow(Long followerId) {
        // 获取当前用户
        final Long userId = LoginUserHolder.getLoginUser().getUserId();
        return Boolean.TRUE.equals(stringRedisTemplate.opsForSet().isMember(FOLLOWS_KEY + userId, followerId.toString()));
//        final Long count = query().eq("user_id", userId).eq("follow_user_id", followerId).eq("is_deleted", 0).count();
//        return count > 0;
    }

    @Override
    public List<UserInfoVo> commonFollows(Long followUserId) {
        // 获取当前用户
        final Long userId = LoginUserHolder.getLoginUser().getUserId();
        final String key = FOLLOWS_KEY + userId;
        final String key2 = FOLLOWS_KEY + followUserId;
        // 利用set的inter求交集
        final Set<String> intersect = stringRedisTemplate.opsForSet().intersect(key, key2);
        if (CollUtil.isEmpty(intersect)) {
            // 无交集
            return Collections.emptyList();
        }

        // 解析交集中的用户id集合
        final List<Long> ids = intersect.stream().map(Long::valueOf).toList();
        // 批量查询用户信息,并转换为UserInfoVo集合，返回
        final List<UserInfo> userInfos = userInfoService.listByIds(ids);
        return CglibUtil.copyList(userInfos, UserInfoVo::new);
    }

    @Override
    public List<UserInfoVo> followList(Long userId) {
        // 获取userId的关注用户ID列表
        List<Long> followUserIds = this.list(
                new LambdaQueryWrapper<>(GroupFollow.class)
                        .eq(GroupFollow::getUserId, userId)
                        .eq(GroupFollow::getIsDeleted, 0)
        ).stream().map(GroupFollow::getFollowUserId).toList();
        // 批量查询用户信息,并转换为UserInfoVo集合，返回
        final List<UserInfo> userInfos = userInfoService.listByIds(followUserIds);
        return CglibUtil.copyList(userInfos, UserInfoVo::new);
    }
}




