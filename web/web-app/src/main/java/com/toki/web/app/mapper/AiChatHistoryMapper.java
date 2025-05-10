package com.toki.web.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.toki.model.entity.AiChatHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * AI聊天历史记录Mapper接口
 *
 * @author toki
 */
@Mapper
public interface AiChatHistoryMapper extends BaseMapper<AiChatHistory> {
    
    /**
     * 根据业务类型和用户ID获取会话ID列表
     *
     * @param type   业务类型
     * @param userId 用户ID
     * @return 会话ID列表
     */
    @Select("SELECT session_key FROM ai_chat_history WHERE type = #{type} AND user_id = #{userId} ORDER BY create_time DESC")
    List<String> selectSessionKeysByTypeAndUserId(@Param("type") String type, @Param("userId") Long userId);
} 