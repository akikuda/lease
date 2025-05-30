package com.toki.common.result;

import lombok.Getter;

/**
 * 统一返回结果状态信息类
 * @author toki
 */
@Getter
public enum ResultCodeEnum {

    SUCCESS(200, "成功"),
    FAIL(201, "失败"),
    PARAM_ERROR(202, "参数不正确"),
    SERVICE_ERROR(203, "服务异常"),
    DATA_ERROR(204, "数据异常"),
    ILLEGAL_REQUEST(205, "非法请求"),
    REPEAT_SUBMIT(206, "重复提交"),
    DELETE_ERROR(207, "请先删除子集"),

    ADMIN_USER_NAME_EXIST_ERROR(300, "用户名已存在"),
    ADMIN_ACCOUNT_EXIST_ERROR(301, "账号已存在"),
    ADMIN_CAPTCHA_CODE_ERROR(302, "验证码错误"),
    ADMIN_CAPTCHA_CODE_EXPIRED(303, "验证码已过期"),
    ADMIN_CAPTCHA_CODE_NOT_FOUND(304, "未输入验证码"),


    ADMIN_LOGIN_AUTH(305, "未登陆"),
    ADMIN_ACCOUNT_NOT_EXIST_ERROR(306, "账号不存在"),
    ADMIN_ACCOUNT_ERROR(307, "用户名或密码错误"),
    ADMIN_ACCOUNT_DISABLED_ERROR(308, "该用户已被禁用"),
    ADMIN_ACCESS_FORBIDDEN(309, "无访问权限"),

    ADMIN_APARTMENT_DELETE_ERROR(310, "请先删除公寓下的房间"),
    SYSTEM_POST_DELETE_ERROR(311, "请先删除该岗位下的员工"),
    LEASE_AGREEMENT_NOT_EXIST_ERROR(312, "房源协议不存在"),

    NICK_USER_NAME_EXIST_ERROR(313, "用户昵称已存在"),
    NICK_USER_NAME_ERROR(314, "用户昵称不合法"),
    AVATAR_UPLOAD_ERROR(315, "头像上传失败"),
    AVATAR_FORMAT_ERROR(316, "头像格式错误"),
    AVATAR_SIZE_ERROR(317, "头像大小超出限制"),
    USER_NOT_EXIST_ERROR(318, "用户不存在"),

    APP_LOGIN_AUTH(501, "未登陆"),
    APP_LOGIN_PHONE_EMPTY(502, "手机号码为空"),
    APP_LOGIN_CODE_EMPTY(503, "验证码为空"),
    APP_SEND_SMS_TOO_OFTEN(504, "验证法发送过于频繁"),
    APP_LOGIN_CODE_EXPIRED(505, "验证码已过期"),
    APP_LOGIN_CODE_ERROR(506, "验证码错误"),
    APP_ACCOUNT_DISABLED_ERROR(507, "该用户已被禁用"),


    TOKEN_EXPIRED(601, "token过期"),
    TOKEN_INVALID(602, "token非法"),

    BLOG_SAVE_ERROR(701, "博文发布失败"),

    MESSAGE_SEND_USER_NOT_EXIST_ERROR(801, "发送用户不存在"),
    MESSAGE_CONTENT_EMPTY_ERROR(802, "发送内容为空"),
    MESSAGE_CONTENT_TOO_LONG_ERROR(807, "发送内容过长"),
    MESSAGE_SEND_ERROR(803, "消息发送失败"),
    MESSAGE_RECEIVE_USER_NOT_EXIST_ERROR(804, "接收用户不存在"),
    MESSAGE_RECEIVE_USER_EMPTY_ERROR(805, "接收用户为空"),
    MESSAGE_SEND_USER_EMPTY_ERROR(806, "发送用户为空");

    private final Integer code;

    private final String message;

    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
