package com.toki.web.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.toki.model.entity.MessageInfo;

import java.util.List;

/**
 * <p>
 * 会话信息表 Mapper 接口
 * </p>
 *
 * @author toki
 * @since 2025-04-25
 */
public interface MessageInfoMapper extends BaseMapper<MessageInfo> {

    List<MessageInfo> selectChatHistory(Long sendUserId, Long receiveUserId, Integer limitMsgLength);

    void updateIsRead(Long sendUserId, Long receiveUserId, List<String> messageIds);

    /**
     * 获取会话用户的id列表,利用联合查询union去重
     *
     * @param userId 用户id
     */
    List<Long> getSessionUserIds(Long userId);

//    @Select("""
//
//            SELECT send_user_id AS id
//            FROM message_info
//            WHERE send_user_id IN (#{sessionUserIds})
//               OR receive_user_id IN (#{sessionUserIds})
//
//            UNION
//
//            SELECT receive_user_id AS id
//            FROM message_info
//            WHERE send_user_id IN (#{sessionUserIds})
//               OR receive_user_id IN (#{sessionUserIds})
//
//            ORDER BY create_time DESC
//
//            """)
//    List<Long> querySortedMessage(List<Long> sessionUserIds);
}
