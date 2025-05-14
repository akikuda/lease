package com.toki.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * AI聊天历史记录实体类
 *
 * @author toki
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("ai_chat_history")
public class AiChatHistory extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "业务类型")
    @TableField(value = "type")
    private String type;

    @Schema(description = "会话ID")
    @TableField(value = "session_id")
    private String sessionId;

    @Schema(description = "用户ID")
    @TableField(value = "user_id")
    private Long userId;

    @Schema(description = "会话内容")
    @TableField(value = "content")
    private String content;
} 