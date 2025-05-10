package com.toki.web.app.vo.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.toki.web.app.vo.user.UserInfoVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author toki
 */
@Data
@Accessors(chain = true)
public class ChatSessionVo {

    @Schema(description = "用户信息")
    private UserInfoVo userInfo;

    @Schema(description = "未读消息数量")
    private Integer unreadCount;

    @Schema(description = "在线标识")
    private Boolean isOnline;

    @Schema(description = "最新一条聊天记录")
    private String lastMessage;

    @Schema(description = "最新一条聊天记录时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastMessageTime;
}
