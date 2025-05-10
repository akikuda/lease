package com.toki.web.app.websocket;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.toki.model.entity.MessageInfo;
import com.toki.web.app.service.MessageInfoService;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket服务
 *
 * @author toki
 */
@Slf4j
@Component
@ServerEndpoint("/webSocket/{userId}")
public class WebSocketServer implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    //     记录当前在线连接数
//    private static int loginCount = 0;
    // 记录当前连接的用户, key为userId, value为WebSocket对象,线程安全
    private static final Map<Long, WebSocket> USER_MAP = new ConcurrentHashMap<>();

    // @ServerEndpoint 注解的类由 WebSocket 容器管理，
    // 而不是 Spring 容器管理，因此 @Autowired 注解不会生效。
    // 静态注入，因为@ServerEndpoint不支持自动注入
    private static MessageInfoService messageService;

    @Autowired
    public void setMessageInfoService(MessageInfoService messageInfoService) {
        WebSocketServer.messageService = messageInfoService;
    }

    private static RedisService redisService;

    @Autowired
    public void setRedisService(RedisService redisService) {
        WebSocketServer.redisService = redisService;
    }

    // 监听send消息队列
//    @RabbitListener(queues = SEND_MESSAGE_QUEUE)
//    public void messageConsumer(MessageInfo message){
//        try {
//            // 转String
//            final String messageJson = JSONUtil.toJsonStr(message);
//            // 监听队列有消息时消费
//            log.warn("messageConsumer接收到消息：{}", message);
//            onMessage(messageJson, null);
//            log.warn("messageConsumer调用onMessage方法成功");
//        } catch (IOException | InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//    }

    /**
     * 连接建立成功调用的方法
     *
     * @param userId  用户ID
     * @param session 会话
     **/
    @OnOpen
    public void onOpen(@PathParam("userId") Long userId, Session session) throws IOException {
        WebSocket webSocket = new WebSocket();
        webSocket.setUserId(userId);
        webSocket.setSession(session);
        boolean containsKey = USER_MAP.containsKey(userId);
        if (!containsKey) {
            // 保存用户，方便后续发送消息给前端
            USER_MAP.put(userId, webSocket);
        }
        if (!redisService.isUserOnline(userId)){
            // 增加当前在线用户数量
//            addLoginCount();
            redisService.addLoginCount();
            // 设置用户在线状态
            redisService.setUserOnline(userId);
        }
        log.warn("连接建立成功!已连接用户: {}, 当前在线人数: {}", userId, redisService.getLoginCount());
        MessageInfo message = new MessageInfo();
        message.setContent(userId + " 已连接");
        sendMessageAll(JSONUtil.toJsonStr(message));
    }

    /**
     * 收到客户端消息后调用的方法，这个消息是连接人发送的消息
     *
     * @param messageInfo 消息内容
     * @param session     会话,可选参数
     **/
    @OnMessage
    public void onMessage(String messageInfo, Session session)
            throws IOException, InterruptedException {
        if (StrUtil.isBlank(messageInfo)) {
            return;
        }
        final MessageInfo message = JSONUtil.toBean(messageInfo, MessageInfo.class);
        // 检查消息是否已经处理过
//        if (redisService.isMessageProcessed(message.getId())) {
//            log.warn("消息已处理过: {}", message.getId());
//            return;
//        }

        // 接收方ID
        final Long receiveUserId = message.getReceiveUserId();
        // 发送方ID
        final Long sendUserId = message.getSendUserId();

        // 未读消息计数
        // 1.判断接收者是否正在查看发送者的消息
        boolean isCurrentChat = messageService.isCurrentChatSession(receiveUserId, sendUserId);
        if (!isCurrentChat) {
            // 2.未读消息计数
            final long unreadCount = messageService.getUnReadCount(receiveUserId, new ArrayList<>(Collections.singleton(sendUserId)));
            // 在消息中添加未读消息数量信息
            final JSONObject jsonObject = JSONUtil.parseObj(message).set("unreadCount", unreadCount);
            sendMessageTo(JSONUtil.toJsonStr(jsonObject), receiveUserId);
        } else {
            // 在发送者的会话中，直接发送消息
            sendMessageTo(messageInfo, receiveUserId);
        }

        // 标记消息为已处理
//        redisService.markMessageAsProcessed(message.getId());

        log.info("{} | {} 接收到消息-> {}", DateUtil.now(), receiveUserId, messageInfo);
    }


    /**
     * 连接关闭调用的方法
     *
     * @param userId      用户ID
     * @param session     会话
     * @param closeReason 关闭原因
     **/
    @OnClose
    public void onClose(@PathParam("userId") Long userId, Session session,
                        CloseReason closeReason) throws IOException {
        boolean containsKey = USER_MAP.containsKey(userId);
        if (containsKey) {
            // 删除map中用户
            USER_MAP.remove(userId);
        }
        if (redisService.isUserOnline(userId)) {
            // 减少断开连接的用户
//            reduceLoginCount();
            redisService.reduceLoginCount();
            // 设置用户离线状态
            redisService.setUserOffline(userId);
        }
        log.warn("关闭连接触发事件!已断开用户: {}, 当前在线人数: {}", userId, redisService.getLoginCount());
        MessageInfo message = new MessageInfo();
        message.setContent(userId + " 已断开");
        sendMessageAll(JSONUtil.toJsonStr(message));
    }

    /**
     * 传输消息错误触发事件
     *
     * @param error 错误信息
     **/
    @OnError
    public void onError(Throwable error) {
        log.info("onError:{}", error.getMessage());
    }

    /**
     * 群发消息给所有在线用户
     *
     * @param message 消息
     **/
    public void sendMessageAll(String message) throws IOException {
        for (WebSocket item : USER_MAP.values()) {
            // 服务器向客户端发送异步消息
            item.getSession().getAsyncRemote().sendText(message);
        }
    }

    /**
     * 单发信息给指定用户
     *
     * @param message   消息
     * @param receiveId 指定用户ID
     **/
    public void sendMessageTo(String message, Long receiveId) throws IOException {
        for (WebSocket item : USER_MAP.values()) {
            if (item.getUserId().equals(receiveId)) {
                System.out.println("用户:" + receiveId + "收到信息:" + message);
                item.getSession().getAsyncRemote().sendText(message);
            }
        }
    }

    // 获取当前连接的用户Map
    public synchronized Map<Long, WebSocket> getUsers() {
        return USER_MAP;
    }

    /**
     * 清空 Redis 中的用户在线状态和在线人数计数
     */
    public static void clearRedisData() {
        if (redisService != null) {
            redisService.clearUserOnlineStatus();
            redisService.clearLoginCount();
        }
    }

    // 启动时清空 Redis 中的用户在线状态和在线人数计数
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        WebSocketServer.applicationContext = applicationContext;
        clearRedisData();
    }

//    // 获取当前在线连接数
//    public static synchronized int getLoginCount() {
//        return loginCount;
//    }
//
//    // 当前在线连接数增加
//    public static synchronized void addLoginCount() {
//        loginCount++;
//    }
//
//    // 当前在线连接数减少
//    public static synchronized void reduceLoginCount() {
//        loginCount--;
//    }
}
