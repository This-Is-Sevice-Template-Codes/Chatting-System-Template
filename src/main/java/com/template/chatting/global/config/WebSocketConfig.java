package com.template.chatting.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // 웹소켓 서버 활성화
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    // 내장 브로커 활성화 (Subscribe 경로)
    // 클라이언트가 구독(Subscribe)할 경로 (서버 -> 클라이언트)
    registry.enableSimpleBroker("/sub");

    // 메시지 핸들러로 라우팅 (Publish 경로)
    // 클라이언트가 발행(Publish)할 경로 (클라이언트 -> 서버)
    // /pub/chat/message 로 보내면 @MessageMapping("/chat/message")가 받음
    registry.setApplicationDestinationPrefixes("/pub");
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    // 클라이언트가 접속할 엔드포인트: ws://localhost:8080/ws-chat
    registry.addEndpoint("/ws-chat")
        .setAllowedOriginPatterns("*");  // CORS 허용 (개발용)
  }
}
