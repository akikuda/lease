package com.toki.web.admin.service;

import com.toki.web.admin.vo.login.CaptchaVo;
import com.toki.web.admin.vo.login.LoginVo;
import com.toki.web.admin.vo.system.user.SystemUserInfoVo;

/**
 * @author toki
 */
public interface LoginService {

    CaptchaVo getCaptcha();

    String login(LoginVo loginVo);

    SystemUserInfoVo getLoginUserInfoById(Long userId);
}
