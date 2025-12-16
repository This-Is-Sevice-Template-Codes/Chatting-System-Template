### 3. 템플릿 코드용 DB 스키마 (MySQL DDL)

해당 스키마는 **1:1, 그룹 채팅, 읽음 처리**까지만 커버할 수 있는 구조입니다. 

**정책:** 모든 테이블은 Soft Delete를 지향하며, 물리적 삭제를 하지 않는다. <br />
**주의:** `chat_member` 테이블에 Unique Key가 없으므로 애플리케이션 레벨에서 중복 입장을 방지해야 한다.

```sql
-- 1. 사용자
CREATE TABLE users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nickname VARCHAR(50) NOT NULL,
    profile_image VARCHAR(255),
    status VARCHAR(20) DEFAULT 'ACTIVE', -- ACTIVE, SUSPENDED, DELETED
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 2. 채팅방
CREATE TABLE chat_room (
    room_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    room_name VARCHAR(100),
    type VARCHAR(20) DEFAULT 'GROUP', 
    status VARCHAR(20) DEFAULT 'ACTIVE', -- ACTIVE, CLOSED
    last_updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 3. 채팅방 멤버 (참여 이력 및 상태)
-- Unique Key 제거됨 -> 동일 유저의 입퇴장 이력이 모두 쌓임
CREATE TABLE chat_member (
    member_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    room_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    
    room_name_override VARCHAR(100),
    last_read_message_id BIGINT DEFAULT 0,
    
    status VARCHAR(20) DEFAULT 'JOINED', -- JOINED, LEFT, KICKED
    joined_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    left_at DATETIME NULL,
    
    -- 조회 성능을 위한 인덱스 (Unique 아님)
    INDEX idx_room_user_status (room_id, user_id, status)
);

-- 4. 메시지 로그
CREATE TABLE chat_message (
    message_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    room_id BIGINT NOT NULL,
    sender_id BIGINT NOT NULL,
    content TEXT,
    msg_type VARCHAR(20) DEFAULT 'TALK',
    status VARCHAR(20) DEFAULT 'VISIBLE', -- VISIBLE, DELETED(가리기)
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_room_msg (room_id, message_id DESC)
);
```