package com.template.chatting.presentation.chat;

import com.template.chatting.application.chat.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

  private final ChatService chatService;

  /**
   * 클라이언트가 "/app/chat/message"로 메시지를 보내면 호출됨.
   * 처리 결과는 "/topic/room1" 구독자들에게 브로드캐스팅됨.
   */
  @MessageMapping("/chat/message")
  @SendTo("/topic/room1")
  public ChatMessageResponse handleMessage(ChatMessageRequest request) {
    // 비즈니스 로직은 서비스에게 위임
    return chatService.saveMessage(request);
  }
}