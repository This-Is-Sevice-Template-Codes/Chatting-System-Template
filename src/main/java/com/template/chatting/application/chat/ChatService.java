package com.template.chatting.application.chat;

import com.template.chatting.infrastructure.db.chat.entity.ChatMember;
import com.template.chatting.infrastructure.db.chat.entity.ChatMessage;
import com.template.chatting.infrastructure.db.chat.entity.MessageType;
import com.template.chatting.infrastructure.db.chat.repository.ChatMemberRepository;
import com.template.chatting.infrastructure.db.chat.repository.ChatMessageRepository;
import com.template.chatting.presentation.chat.ChatMessageRequest;
import com.template.chatting.presentation.chat.ChatMessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

  private final ChatMessageRepository messageRepository;
  private final ChatMemberRepository memberRepository;
  private final SimpMessagingTemplate messagingTemplate;

  /**
   * 메시지 저장 및 전송
   */
  @Transactional
  public void sendMessage(ChatMessageRequest request) {
    // 1. DB 저장
    ChatMessage savedEntity = messageRepository.save(request.toEntity());

    // 2. Response 변환
    ChatMessageResponse response = ChatMessageResponse.from(savedEntity);

    // 3. 브로드캐스팅
    messagingTemplate.convertAndSend("/sub/chat/room/" + response.getRoomId(), response);
  }

  /**
   * 입장 처리 (Soft Delete Logic)
   */
  @Transactional
  public void enterRoom(ChatMessageRequest request) {
    Long roomId = request.getRoomId();
    Long userId = request.getSenderId();

    // 1. 멤버 조회 (없으면 생성)
    ChatMember member = memberRepository.findByRoomIdAndUserId(roomId, userId)
        .orElse(ChatMember.builder()
            .roomId(roomId)
            .userId(userId)
            .status("NEW") // 신규 마킹
            .build());

    // 2. 상태 처리
    if ("JOINED".equals(member.getStatus())) {
      log.info("User {} is already in Room {}", userId, roomId);
      return; // 이미 참여 중
    }

    // 신규이거나 LEFT 상태면 JOINED로 변경
    if ("NEW".equals(member.getStatus())) {
      member = ChatMember.builder()
          .roomId(roomId)
          .userId(userId)
          .status("JOINED")
          .build();
    } else {
      member.rejoin(); // 재입장
    }
    memberRepository.save(member);

    // 3. 입장 알림 메시지 전송
    ChatMessageRequest enterMsg = ChatMessageRequest.builder()
        .roomId(roomId)
        .senderId(userId)
        .content("입장하였습니다.")
        .msgType(MessageType.ENTER)
        .build();
    sendMessage(enterMsg);
  }
}