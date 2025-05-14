package com.toki.web.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.toki.model.entity.AiChatHistory;

import java.util.List;

/**
 * @author toki
 */
public interface AiChatHistoryService extends IService<AiChatHistory> {

    /**
     * 保存会话记录到数据库
     *
     * @param type       业务类型,如chat、service、pdf
     * @param sessionKey 会话ID
     * @param userId     用户ID
     * @param content    AI响应内容
     */
    void save(String type, String sessionKey, Long userId, String content);

    /**
     * 保存会话ID记录到数据库
     *
     * @param type       业务类型,如chat、service、pdf
     * @param sessionKey 会话ID
     * @param userId     用户ID
     */
    void save(String type, String sessionKey, Long userId);

    /**
     * 获取会话记录列表
     *
     * @param type   业务类型
     * @param userId 用户ID
     * @return 会话ID列表
     */
    List<String> getSessionIds(String type, Long userId);

}
