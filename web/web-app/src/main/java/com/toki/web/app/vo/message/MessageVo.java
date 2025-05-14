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
    private String sessionId;

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

        String[] strs = message.getText().split("</think>");
        this.content = strs.length == 2 ? strs[1] : strs[0];
    }
}
