package com.template.chatting.infrastructure.db.chat.repository;

import com.template.chatting.infrastructure.db.chat.entity.ChatMember;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMemberRepository extends JpaRepository<ChatMember, Long> {
  Optional<ChatMember> findByRoomIdAndUserId(Long roomId, Long userId);
}