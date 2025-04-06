package com.toki.common.utils;

/**
 * @author toki
 */
public class LoginUserHolder {

    private static final ThreadLocal<LoginUser> THREAD_LOCAL = new ThreadLocal<>();

    public static void setLoginUser(LoginUser loginUser) {
        THREAD_LOCAL.set(loginUser);
    }

    public static LoginUser getLoginUser() {
        return THREAD_LOCAL.get();
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }
}
