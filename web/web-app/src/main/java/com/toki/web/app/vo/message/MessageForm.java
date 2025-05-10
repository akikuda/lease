package com.toki.web.app.vo.message;

import com.toki.model.entity.MessageInfo;
import com.toki.web.app.vo.user.UserInfoVo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * @author toki
 */
@Accessors(chain = true)
@Data
public class MessageForm {

  // 发送用户和接收用户完整聊天消息列表
  private List<MessageInfo> messages = new ArrayList<>();
  // 未读消息数量
  private Integer noReadMessageLength;
  // 在线标识
  private Boolean isOnline;
  // 发送信息用户
  private UserInfoVo sendUser;
  // 接收信息用户,偏向于赋值登录人员用户信息
  private UserInfoVo receiveUser;
  // 最新一条聊天记录
  private String lastMessage;
}