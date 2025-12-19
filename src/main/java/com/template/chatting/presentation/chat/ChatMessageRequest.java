package com.template.chatting.presentation.chat;

import com.template.chatting.infrastructure.db.chat.entity.MessageType;
import com.template.chatting.infrastructure.db.chat.entity.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageRequest {

  private Long roomId;
  private Long senderId; // 실제로는 Security Context/Session에서 꺼내는 게 정석
  private String content;

  @Builder.Default
  private MessageType msgType = MessageType.TALK;

  // Request -> Entity 변환 메서드 (Factory Method)
  public ChatMessage toEntity() {
    return ChatMessage.builder()
        .roomId(this.roomId)
        .senderId(this.senderId)
        .content(this.content)
        .msgType(this.msgType)
        .build();
  }
}