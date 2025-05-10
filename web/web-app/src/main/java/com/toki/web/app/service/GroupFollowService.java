package com.toki.web.app.service;

import com.toki.model.entity.GroupFollow;
import com.baomidou.mybatisplus.extension.service.IService;
import com.toki.web.app.vo.user.UserInfoVo;

import java.util.List;

/**
* @author toki
*/
public interface GroupFollowService extends IService<GroupFollow> {

    /**
     * 当前用户去 关注 or 取关 指定用户
     * @param followUserId 指定用户
     * @param isFollow true 关注，false 取关
     * */
    boolean follow(Long followUserId, Boolean isFollow);

    /**
     * 查询当前用户是否关注指定用户
     *
     * @param followerId 指定用户
     * */
    boolean isFollow(Long followerId);

    /**
     * 查询当前用户与指定用户的共同关注
     *
     * @param followUserId 指定用户
     * */
    List<UserInfoVo> commonFollows(Long followUserId);

    /**
     * 查询指定用户的关注列表的用户信息
     * */
    List<UserInfoVo> followList(Long userId);

}
