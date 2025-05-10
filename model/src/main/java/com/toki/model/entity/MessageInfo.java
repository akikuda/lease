package com.toki.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.toki.model.enums.MessageStatus;
import com.toki.model.enums.MessageType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 会话信息表
 * </p>
 *
 * @author toki
 * @since 2025-04-25
 */
@Data
@Accessors(chain = true)
@TableName("message_info")
@Schema(description = "会话信息表")
public class MessageInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @Schema(description = "发送人id")
    @TableField("send_user_id")
    private Long sendUserId;

    @Schema(description = "接收人id")
    @TableField("receive_user_id")
    private Long receiveUserId;

    // 数据量较大可单独分成一张表
    @Schema(description = "会话内容")
    @TableField("content")
    private String content;

    @Schema(description = "是否已读,0代表未读")
    @TableField("is_read")
    private MessageStatus isRead;

    @Schema(description = "是否为AI对话,0代表用户对话,1代表AI对话")
    @TableField("is_ai")
    private MessageType isAi;

    @Schema(description = "留言时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonIgnore
    private Date createTime;

    @Schema(description = "逻辑删除")
    @TableField("is_deleted")
    @JsonIgnore
    @TableLogic
    private Byte isDeleted;

}
