package com.template.chatting.infrastructure.db.chat.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chat_member", indexes = {
    @Index(name = "idx_room_user_status", columnList = "roomId, userId")
})
@EntityListeners(AuditingEntityListener.class)
public class ChatMember {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long memberId;

  @Column(nullable = false)
  private Long roomId;

  @Column(nullable = false)
  private Long userId;

  @Builder.Default
  private String status = "JOINED"; // JOINED, LEFT

  @CreatedDate
  private LocalDateTime joinedAt;

  private LocalDateTime leftAt;

  // 비즈니스 로직 - 재입장
  public void rejoin() {
    this.status = "JOINED";
    this.joinedAt = LocalDateTime.now(); // 재입장 시간 갱신
    this.leftAt = null;
  }

  public void leave() {
    this.status = "LEFT";
    this.leftAt = LocalDateTime.now();
  }
}