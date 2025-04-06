package com.toki.web.admin.service;

import com.toki.web.admin.vo.login.CaptchaVo;
import com.toki.web.admin.vo.login.LoginVo;

/**
 * @author toki
 */
public interface LoginService {

    CaptchaVo getCaptcha();

    String login(LoginVo loginVo);
}
