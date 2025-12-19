package com.template.chatting.presentation.chat;

import com.template.chatting.infrastructure.db.chat.entity.ChatMessage;
import com.template.chatting.infrastructure.db.chat.entity.MessageType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponse {

  private Long messageId;
  private Long roomId;
  private Long senderId;
  private String content;
  private MessageType msgType;
  private String sendTime; // 화면 표시용 포맷팅된 시간 (예: "오전 10:20")

  // Entity -> Response 변환 메서드 (Static Factory Method)
  public static ChatMessageResponse from(ChatMessage entity) {
    return ChatMessageResponse.builder()
        .messageId(entity.getMessageId())
        .roomId(entity.getRoomId())
        .senderId(entity.getSenderId())
        .content(entity.getContent())
        .msgType(entity.getMsgType())
        .sendTime(formatTime(entity.getCreatedAt()))
        .build();
  }

  private static String formatTime(LocalDateTime time) {
    if (time == null) return "";
    return time.format(DateTimeFormatter.ofPattern("HH:mm"));
  }
}