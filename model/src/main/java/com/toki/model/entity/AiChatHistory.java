package com.toki.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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

    @Schema(description = "会话标识")
    @TableField(value = "session_key")
    private String sessionKey;

    @Schema(description = "用户ID")
    @TableField(value = "user_id")
    private Long userId;
} 