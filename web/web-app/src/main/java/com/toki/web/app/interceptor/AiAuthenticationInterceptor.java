//package com.toki.web.app.interceptor;
//
//import com.toki.common.utils.JwtUtil;
//import com.toki.common.utils.LoginUser;
//import com.toki.common.utils.LoginUserHolder;
//import io.jsonwebtoken.Claims;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//@Component
//public class AiAuthenticationInterceptor implements HandlerInterceptor {
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        try {
//            // 获取token
//            final String token = request.getHeader("access-token");
//            if (token != null && !token.isEmpty()) {
//                // 解析token
//                final Claims parsedToken = JwtUtil.parseToken(token);
//                final Long userId = parsedToken.get("userId", Long.class);
//                final String username = parsedToken.get("username", String.class);
//                // 存入ThreadLocal中
//                LoginUserHolder.setLoginUser(new LoginUser(userId, username));
//            } else {
//                // 如果没有token，尝试从请求参数获取userId
//                String userIdParam = request.getParameter("userId");
//                if (userIdParam != null && !userIdParam.isEmpty()) {
//                    Long userId = Long.parseLong(userIdParam);
//                    LoginUserHolder.setLoginUser(new LoginUser(userId, null));
//                }
//            }
//        } catch (Exception e) {
//            // 解析失败不阻止请求，记录日志
//            // 尝试从请求参数获取userId
//            String userIdParam = request.getParameter("userId");
//            if (userIdParam != null && !userIdParam.isEmpty()) {
//                Long userId = Long.parseLong(userIdParam);
//                LoginUserHolder.setLoginUser(new LoginUser(userId, null));
//            }
//        }
//        return true;
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        LoginUserHolder.remove();
//    }
//}