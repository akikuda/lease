package com.toki.web.admin.controller.login;


import com.toki.common.result.Result;
import com.toki.common.utils.LoginUser;
import com.toki.common.utils.LoginUserHolder;
import com.toki.web.admin.service.LoginService;
import com.toki.web.admin.vo.login.CaptchaVo;
import com.toki.web.admin.vo.login.LoginVo;
import com.toki.web.admin.vo.system.user.SystemUserInfoVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author toki
 */
@Tag(name = "后台管理系统登录管理")
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @Operation(summary = "获取图形验证码")
    @GetMapping("login/captcha")
    public Result<CaptchaVo> getCaptcha() {
        return Result.ok(loginService.getCaptcha());
    }

    @Operation(summary = "登录")
    @PostMapping("login")
    public Result<String> login(@RequestBody LoginVo loginVo) {
        return Result.ok(loginService.login(loginVo));
    }

    @Operation(summary = "获取登陆用户个人信息")
    @GetMapping("info")
    public Result<SystemUserInfoVo> info() {
        return Result.ok(
                loginService
                .getLoginUserInfoById(LoginUserHolder.getLoginUser().getUserId())
        );
    }
}