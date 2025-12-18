package com.template.chatting.presentation.chat;

import com.template.chatting.infrastructure.db.chat.entity.ChatMessage;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatMessageResponse {
  private String sender;
  private String content;
  private LocalDateTime sendTime;

  // Entity -> DTO 변환 생성자
  public ChatMessageResponse(ChatMessage entity) {
    this.sender = entity.getSender();
    this.content = entity.getContent();
    this.sendTime = entity.getSendTime();
  }
}