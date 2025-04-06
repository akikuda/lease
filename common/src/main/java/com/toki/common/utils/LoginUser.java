package com.toki.common.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author toki
 */
@Data
@AllArgsConstructor
public class LoginUser {

    private Long userId;
    private String username;
}