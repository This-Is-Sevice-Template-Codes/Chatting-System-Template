package com.template.chatting.application.chat;

import com.template.chatting.infrastructure.db.chat.entity.ChatMessage;
import com.template.chatting.infrastructure.db.chat.repository.ChatMessageRepository;
import com.template.chatting.presentation.chat.ChatMessageRequest;
import com.template.chatting.presentation.chat.ChatMessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

  private final ChatMessageRepository chatMessageRepository;

  @Transactional // 쓰기 트랜잭션
  public ChatMessageResponse saveMessage(ChatMessageRequest request) {
    // 1. DTO(Request) -> Entity 변환
    ChatMessage message = new ChatMessage(request.getSender(), request.getContent());

    // 2. 저장 (이 시점에 Auditing이 동작하여 sendTime 생성)
    ChatMessage savedMessage = chatMessageRepository.save(message);

    // 3. Entity -> DTO(Response) 변환 후 반환
    return new ChatMessageResponse(savedMessage);
  }
}