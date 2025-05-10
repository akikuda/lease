package com.toki.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * @author toki
 */

public enum MessageType implements BaseEnum {


    USER(0, "未读"),

    AI(1, "已读");


    @EnumValue
    @JsonValue
    private Integer code;
    private String name;

    MessageType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
