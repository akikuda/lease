package com.toki.web.app.controller.login;


import com.toki.common.result.Result;
import com.toki.common.utils.LoginUserHolder;
import com.toki.web.app.service.LoginService;
import com.toki.web.app.vo.user.LoginVo;
import com.toki.web.app.vo.user.UserInfoVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author toki
 */
@Tag(name = "登录管理")
@RestController
@RequestMapping("/app/")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("login/getCode")
    @Operation(summary = "获取短信验证码")
    public Result getCode(@RequestParam String phone) {
        loginService.getSMSCode(phone);
        return Result.ok();
    }

    @PostMapping("login")
    @Operation(summary = "登录")
    public Result<String> login(@RequestBody LoginVo loginVo) {
        return Result.ok(loginService.login(loginVo));
    }

    @GetMapping("info")
    @Operation(summary = "获取登录用户信息")
    public Result<UserInfoVo> info() {
        return Result.ok(
                loginService.getLoginUserInfoById(LoginUserHolder.getLoginUser().getUserId())
        );
    }

    @GetMapping("getInfo")
    @Operation(summary = "获取指定用户信息")
    public Result<UserInfoVo> info(@RequestParam("id") Long id) {
        return Result.ok(loginService.getLoginUserInfoById(id));
    }
}

