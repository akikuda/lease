package com.toki.web.app.websocket;

import jakarta.websocket.Session;
import lombok.Data;

/**
 * @author toki
 */
@Data
public class WebSocket {

    private Session session;
    private Long userId;

    // 最后一次心跳时间
    private long lastHeartbeatTime;
}