package com.toki.web.app.controller.group;

import com.toki.common.result.Result;
import com.toki.web.app.service.GroupFollowService;
import com.toki.web.app.vo.user.UserInfoVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author toki
 * @since 2025-04-25
 */
@Tag(name = "用户关注")
@RestController
@RequestMapping("/app/follow")
@RequiredArgsConstructor
public class FollowController {

    private final GroupFollowService followService;

    @Operation(summary = "关注或取消关注用户")
    @PutMapping("/{id}/{isFollow}")
    public Result<Boolean> follow(@PathVariable("id") Long followUserId, @PathVariable("isFollow") Boolean isFollow) {
        return Result.ok(followService.follow(followUserId, isFollow));
    }

    @Operation(summary = "查询是否关注指定用户")
    @GetMapping("/or/not/{id}")
    public Result<Boolean> isFollow(@PathVariable("id") Long followUserId) {
        return Result.ok(followService.isFollow(followUserId));
    }

    @Operation(summary = "查询与指定用户的共同关注")
    @GetMapping("/common/{id}")
    public Result<List<UserInfoVo>> commonFollows(@PathVariable("id") Long followUserId) {
        List<UserInfoVo> commonFollowUsers = followService.commonFollows(followUserId);
        return Result.ok(commonFollowUsers);
    }

    @Operation(summary = "查询指定用户关注的用户信息列表")
    @GetMapping("/list/{id}")
    public Result<List<UserInfoVo>> followList(@PathVariable("id") Long id) {
        return Result.ok(followService.followList(id));
    }

}
