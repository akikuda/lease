package com.toki.web.app.vo.message;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.messages.Message;

/**
 * @author toki
 */
@NoArgsConstructor
@Data
public class MessageVo {

    private String content;
    private String role;

    public MessageVo(Message message) {
        switch (message.getMessageType()) {
            case USER:
                this.role = "user";
                break;
            case ASSISTANT:
                this.role = "assistant";
                break;
            default:
                this.role = "";
        }
        this.content = message.getText();
    }
}
