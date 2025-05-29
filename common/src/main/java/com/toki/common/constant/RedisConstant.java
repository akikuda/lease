package com.toki.common.constant;

import java.time.Duration;

/**
 * @author toki
 */
public class RedisConstant {
    public static final String ADMIN_LOGIN_PREFIX = "admin:login:";
    public static final Integer ADMIN_LOGIN_CAPTCHA_TTL_SEC = 60;
    public static final String APP_LOGIN_PREFIX = "app:login:";
    public static final Integer APP_LOGIN_CODE_RESEND_TIME_SEC = 60;
    public static final Integer APP_LOGIN_CODE_TTL_SEC = 60 * 10;
    public static final String APP_ROOM_PREFIX = "app:room:";

    public static final String FEED_KEY = "feed:";
    public static final String BLOG_KEY = "blog:";
    public static final String BLOG_LIKED_KEY = "blog:liked:";
    public static final String BLOG_USER_KEY = "blog:user:";
    public static final String FOLLOWS_KEY = "follows:";
    public static final String CHAT_KEY = "chat:";
    public static final String LOGIN_COUNT_KEY = "loginCount:";
    public static final String ONLINE_USERS_KEY = "onlineUsers:";
    public static final String SESSION_ID_KEY = "sessionId:";

    public static final String AI_CHAT_KEY = "ai:";
    public static final String AI_MEMORY_KEY = "ai:memory:";

    // Redis过期时间(7天)
    public static final Duration AI_CHAT_TTL = Duration.ofDays(1);
    public static final Integer AI_MEMORY_TTL = 60;

    public static final Integer NULL_EXPIRE_TIME_SEC = 60 * 3;

    public static final String MESSAGE_PROCESSED = "message:processed:";
}