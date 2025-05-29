package com.toki.web.app.websocket;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.gson.JsonObject;
import com.toki.common.exception.LeaseException;
import com.toki.model.entity.MessageInfo;
import com.toki.web.app.service.MessageInfoService;
import jakarta.annotation.PreDestroy;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.toki.common.constant.RabbitMqConstant.MESSAGE_FANOUT;
import static com.toki.common.constant.RabbitMqConstant.MESSAGE_KEY;
import static com.toki.common.result.ResultCodeEnum.MESSAGE_CONTENT_EMPTY_ERROR;
import static com.toki.common.result.ResultCodeEnum.MESSAGE_CONTENT_TOO_LONG_ERROR;
import static com.toki.model.enums.MessageStatus.READ;
import static com.toki.model.enums.MessageStatus.UNREAD;

/**
 * WebSocket服务
 *
 * @author toki
 */
@Slf4j
@Component
@ServerEndpoint("/webSocket/{userId}")
public class WebSocketServer implements ApplicationContextAware {


    //     记录当前在线连接数
//    private static int loginCount = 0;

    // 添加一个静态的同步列表来存储消息，以及一个阈值来决定何时批量发送消息
    // 这里使用了Collections.synchronizedList来保证线程安全，防止多线程同时操作MESSAGE_BATCH时，数据被覆盖或同时扩容时size更新不一致
//    private static final List<MessageInfo> MESSAGE_BATCH = Collections.synchronizedList(new ArrayList<>());
//    private static final int BATCH_SIZE = 1;

    // 记录当前连接的用户, key为userId, value为WebSocket对象,线程安全
    private static final Map<Long, WebSocket> USER_MAP = new ConcurrentHashMap<>();

    // 用于检测超时连接的线程池,执行延迟任务
    private static final ScheduledExecutorService HEARTBEAT_CHECKER =
            Executors.newSingleThreadScheduledExecutor();

    // 心跳超时时间(毫秒)，应大于前端发送间隔2倍，这里设置为60秒
    private static final long HEARTBEAT_TIMEOUT = 60000;

    static {
        // 启动定时检测任务，每30秒检测一次
        HEARTBEAT_CHECKER.scheduleAtFixedRate(() -> {
            long now = System.currentTimeMillis();
            USER_MAP.forEach((id, client) -> {
                // 如果超过心跳超时时间未收到心跳，关闭连接
                if (now - client.getLastHeartbeatTime() > HEARTBEAT_TIMEOUT) {
                    try {
                        log.warn("用户{}心跳超时，关闭连接", id);
                        client.getSession().close(new CloseReason(CloseReason.CloseCodes.GOING_AWAY, "心跳超时"));
                    } catch (IOException e) {
                        log.error("关闭心跳超时连接失败", e);
                    }
                }
            });
        }, 30, 30, TimeUnit.SECONDS);
    }

    // @ServerEndpoint 注解的类由 WebSocket 容器管理，
    // 而不是 Spring 容器管理，因此 @Autowired 注解不会生效。
    // 静态注入，因为@ServerEndpoint不支持自动注入
    private static MessageInfoService messageService;
    private static RedisService redisService;
    private static RabbitTemplate rabbitTemplate;
    private static ApplicationContext applicationContext;

    @Autowired
    public void setMessageInfoService(MessageInfoService messageInfoService) {
        WebSocketServer.messageService = messageInfoService;
    }

    @Autowired
    public void setRedisService(RedisService redisService) {
        WebSocketServer.redisService = redisService;
    }

    @Autowired
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        WebSocketServer.rabbitTemplate = rabbitTemplate;
    }

    // 后端服务启动时清空 Redis 中的用户在线状态和在线人数计数
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        WebSocketServer.applicationContext = applicationContext;
        clearRedisData();

        // 注册应用关闭事件监听器
//        if (applicationContext instanceof ConfigurableApplicationContext configurableContext) {
//            configurableContext.addApplicationListener(event -> {
//                if (event instanceof ContextClosedEvent) {
//                    try {
//                        // 在应用关闭时执行批量消息发送
//                        if (!MESSAGE_BATCH.isEmpty()) {
//                            sendBatchToQueue();
//                        }
//                    } catch (IOException e) {
//                        log.error("应用关闭时发送批量消息失败", e);
//                    }
//                }
//            });
//        }
    }

    // 后端服务关闭时，将剩余的消息保存到数据库，确保没有消息丢失
//    @PreDestroy
//    public void shutdown() throws IOException {
//        if (!MESSAGE_BATCH.isEmpty()) {
//            sendBatchToQueue();
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
        // 初始化
        WebSocket webSocket = new WebSocket();
        webSocket.setUserId(userId);
        webSocket.setSession(session);
        webSocket.setLastHeartbeatTime(System.currentTimeMillis());

        boolean containsKey = USER_MAP.containsKey(userId);
        if (!containsKey) {
            // 保存用户，方便后续发送消息给前端
            USER_MAP.put(userId, webSocket);
            // 设置用户在线
            redisService.setUserOnline(userId);
        }
//        if (!redisService.isUserOnline(userId)) {
        // 利用Lua脚本保证原子性
//            redisService.addLoginCountAndUserOnline(userId);
        // 增加当前在线用户数量
//            addLoginCount();
//            redisService.addLoginCount();
        // 设置用户在线状态
//            redisService.setUserOnline(userId);
//        }
        log.warn("连接建立成功!已连接用户: {}, 当前在线人数: {}", userId, redisService.getLoginCount());
        MessageInfo message = new MessageInfo();
        message.setContent(userId + " 已连接");
        // 发送消息给所有用户，通知有新用户连接
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

        // 处理心跳包(带有type字段且值为ping)
        final JSONObject messageJson = JSONUtil.parseObj(messageInfo);
        if (messageJson.containsKey("type") && "ping".equals(messageJson.getStr("type"))) {
            // 更新指定用户的最后一次心跳时间
            final Long userId = Long.valueOf(messageJson.getStr("userId"));
            if (USER_MAP.containsKey(userId)) {
                USER_MAP.get(userId).setLastHeartbeatTime(System.currentTimeMillis());
            }
            // 可选：回应心跳包,这里不回应
            return;
        }
        // 将json对象转为MessageInfo对象
        final MessageInfo message = messageJson.toBean(MessageInfo.class);


        // 检查消息是否已经处理过
//        if (redisService.isMessageProcessed(message.getId())) {
//            log.warn("消息已处理过: {}", message.getId());
//            return;
//        }

        // 接收方ID
        final Long receiveUserId = message.getReceiveUserId();
        // 发送方ID
        final Long sendUserId = message.getSendUserId();

        /*
         * 批量保存消息到数据库方案，有问题，待解决
         * */
//        // 单条消息的数据校验
//        messageService.userStatusCheck(sendUserId, receiveUserId);
//        if (StrUtil.isBlank(message.getContent())) {
//            throw new LeaseException(MESSAGE_CONTENT_EMPTY_ERROR);
//        }
//        // 限制单条消息长度
//        if (message.getContent().length() > 5000) {
//            throw new LeaseException(MESSAGE_CONTENT_TOO_LONG_ERROR);
//        }
//        // 1.判断接收者是否正在查看发送者的消息
//        boolean isCurrentChat = messageService.isCurrentChatSession(receiveUserId, sendUserId);
//        // 设置消息已读状态
//        message.setIsRead(isCurrentChat ? READ : UNREAD);
//
//        // 将消息添加到批量集合中
//        MESSAGE_BATCH.add(message);
//        // 判断集合是否达到批量发送到队列的阈值
//        if (MESSAGE_BATCH.size() >= BATCH_SIZE) {
//            log.warn("消息集合达到阈值，开始发送消息到MQ队列,集合：{}", MESSAGE_BATCH);
//            sendBatchToQueue();
//        }

        // 1.判断接收者是否正在查看发送者的消息

        /*
         * 单条消息保存到数据库方案
         * */
        boolean isCurrentChat = messageService.isCurrentChatSession(receiveUserId, sendUserId);
        // 提前设置消息是否已读
        message.setIsRead(isCurrentChat ? READ : UNREAD);
        // 发送消息到MQ队列
        rabbitTemplate.convertAndSend(MESSAGE_FANOUT, "", message);

        // 发送消息
        if (!isCurrentChat) {
            // 2.未读消息计数
            final long unreadCount = messageService.getUnReadCount(receiveUserId, new ArrayList<>(Collections.singleton(sendUserId)));
            // 在消息中添加未读消息数量信息,给前端解析显示
            final JSONObject jsonObject = JSONUtil.parseObj(message).set("unreadCount", unreadCount);
            sendMessageTo(JSONUtil.toJsonStr(jsonObject), receiveUserId);
        } else {
            // 在发送者的会话中，直接发送消息
            sendMessageTo(messageInfo, receiveUserId);
        }

        // 标记消息为已处理
//        redisService.markMessageAsProcessed(message.getId());

        log.warn("{} | {} 接收到消息-> {}", DateUtil.now(), receiveUserId, messageInfo);
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
            redisService.setUserOffline(userId);
        }
//        if (redisService.isUserOnline(userId)) {
//            // 利用Lua脚本保证原子性
//            redisService.reduceLoginCountAndUserOffline(userId);
//        }
        log.warn("关闭连接触发事件!已断开用户: {}, 当前在线人数: {}", userId, redisService.getLoginCount());

        if(USER_MAP.isEmpty() && !HEARTBEAT_CHECKER.isShutdown()){
            // 关闭定时检测任务
            HEARTBEAT_CHECKER.shutdown();
            log.info("关闭定时检测任务");
        }

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
//            redisService.clearLoginCount();
        }
    }

//    // 将消息集合发送到MQ批量异步保存，并清空集合以便接收新的消息
//    private void sendBatchToQueue() throws IOException {
//        synchronized (MESSAGE_BATCH) {
//            if (!MESSAGE_BATCH.isEmpty()) {
//                List<MessageInfo> batch = new ArrayList<>(MESSAGE_BATCH);
//
//                // 按创建时间排序
//                batch.sort(Comparator.comparing(MessageInfo::getCreateTime));
//
//                MESSAGE_BATCH.clear();
//                rabbitTemplate.convertAndSend(MESSAGE_FANOUT, "", batch);
//            }
//        }
//    }
//
//    @Scheduled(fixedDelay = 5000) // 每5秒执行一次
//    public void scheduledSendMessages() throws IOException {
//        synchronized (MESSAGE_BATCH) {
//            if (!MESSAGE_BATCH.isEmpty()) {
//                log.warn("定时任务触发，发送剩余消息到MQ队列,集合：{}", MESSAGE_BATCH);
//                sendBatchToQueue();
//            }
//        }
//    }


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
