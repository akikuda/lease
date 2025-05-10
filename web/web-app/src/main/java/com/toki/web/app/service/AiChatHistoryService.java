package com.toki.web.app.service;

import java.util.List;

/**
 * @author toki
 */
public interface AiChatHistoryService {

    /**
     * 保存会话记录
     *
     * @param type       业务类型,如chat、service、pdf
     * @param sessionKey 会话ID
     * @param userId     用户ID
     */
    void save(String type, String sessionKey, Long userId);

    /**
     * 获取会话记录
     *
     * @param type   业务类型
     * @param userId 用户ID
     * @return 会话ID列表
     */
    List<String> getChatIds(String type, Long userId);

}
