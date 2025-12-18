package com.template.chatting.infrastructure.db.chat.repository;

import com.template.chatting.infrastructure.db.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
}