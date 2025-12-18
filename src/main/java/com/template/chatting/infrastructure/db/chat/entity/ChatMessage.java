package com.template.chatting.infrastructure.db.chat.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class) // JPA Auditing 리스너 등록
public class ChatMessage {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String sender;
  private String content;

  @CreatedDate // 저장 시 시간 자동 주입
  @Column(updatable = false)
  private LocalDateTime sendTime;

  // 생성자 (ID와 시간은 자동 생성이므로 제외)
  public ChatMessage(String sender, String content) {
    this.sender = sender;
    this.content = content;
  }
}