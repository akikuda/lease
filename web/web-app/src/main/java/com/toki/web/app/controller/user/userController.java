package com.toki.web.app.controller.user;

import cn.hutool.core.util.StrUtil;
import com.toki.common.exception.LeaseException;
import com.toki.common.result.Result;
import com.toki.common.utils.LoginUserHolder;
import com.toki.web.app.service.UserInfoService;
import com.toki.web.app.vo.user.UserInfoVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

import static com.toki.common.result.ResultCodeEnum.*;

/**
 * @author toki
 */
@Tag(name = "个人中心")
@RestController
@RequestMapping("/app/user")
@RequiredArgsConstructor
public class userController {

    private final UserInfoService userService;

    /**
     * 更新用户昵称，数据库中设置nickname是非空唯一的
     */
    @Operation(summary = "更新用户昵称")
    @PutMapping("/update/nickname")
    public Result updateUserNickname(@RequestParam("nickname") String nickname) {

        if (StrUtil.isBlank(nickname)){
            throw new LeaseException(NICK_USER_NAME_ERROR);
        }

        final boolean isSuccess = userService.update()
                .set("nickname", nickname)
                .eq("id", LoginUserHolder.getLoginUser().getUserId())
                .update();
        if (!isSuccess) {
            throw new LeaseException(NICK_USER_NAME_EXIST_ERROR);
        }
        return Result.ok(true);
    }

    @Operation(summary = "更新用户头像URL")
    @PutMapping("/update/avatar")
    public Result updateUserAvatarUrl(@RequestParam("avatarUrl") String avatarUrl) {

        if (StrUtil.isBlank(avatarUrl)){
            throw new LeaseException(AVATAR_FORMAT_ERROR);
        }

        // 更新头像URL
        final boolean isSuccess = userService.update()
                .set("avatar_url", avatarUrl)
                .set("update_time", new Date())
                .eq("id", LoginUserHolder.getLoginUser().getUserId())
                .update();
        return Result.ok(isSuccess);
    }


}


