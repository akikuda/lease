package com.toki.web.app.interceptor;

import com.toki.common.utils.JwtUtil;
import com.toki.common.utils.LoginUser;
import com.toki.common.utils.LoginUserHolder;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author toki
 */
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取token
        final String token = request.getHeader("access-token");
        // 解析token
        final Claims parsedToken = JwtUtil.parseToken(token);
        final Long userId = parsedToken.get("userId", Long.class);
        final String username = parsedToken.get("username", String.class);
        // 存入ThreadLocal中，供后续业务使用，避免重复解析token
        LoginUserHolder.setLoginUser(new LoginUser(userId, username));
        // 放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        LoginUserHolder.remove();
    }
}
