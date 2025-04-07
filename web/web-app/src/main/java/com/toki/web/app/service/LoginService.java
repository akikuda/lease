package com.toki.web.app.service;

import com.toki.web.app.vo.user.LoginVo;
import com.toki.web.app.vo.user.UserInfoVo;

/**
 * @author toki
 */
public interface LoginService {

    void getSMSCode(String phone);

    String login(LoginVo loginVo);

    UserInfoVo getLoginUserInfoById(Long userId);
}
