package com.template.chatting.presentation.chat;

import com.template.chatting.application.chat.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

  private final ChatService chatService;

  /**
   * [메시지 전송]
   * Client: SEND /pub/chat/message
   * Body: { "roomId": 1, "senderId": 100, "content": "hello" }
   */
  @MessageMapping("/chat/message")
  public void sendMessage(@Payload ChatMessageRequest request) {
    // 1. DB 저장 및 비즈니스 로직
    // 2. 내부에서 messagingTemplate.convertAndSend() 호출하여 브로드캐스팅
    chatService.sendMessage(request);
  }

  /**
   * [입장]
   * Client: SEND /pub/chat/enter
   */
  @MessageMapping("/chat/enter")
  public void enterRoom(@Payload ChatMessageRequest request) {
    chatService.enterRoom(request);
  }
}