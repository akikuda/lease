package com.toki.web.app.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author toki
 */
@Schema(description = "用户基本信息")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoVo {

    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "用户头像")
    private String avatarUrl;

}