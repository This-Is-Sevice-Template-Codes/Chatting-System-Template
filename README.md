. 📝 기술 명세서 및 요구사항 (PRD)
프로젝트명: Real-time Multi-User Chat Service (MVP)

1.1 기술 스택 (Tech Stack)
Language: Java 21 (LTS)

Framework: Spring Boot 3.2+ (WebFlux 기반)

Protocol: WebSocket + STOMP (Simple Text Oriented Messaging Protocol)

Database: MySQL 8.0 (R2DBC Driver 사용 - r2dbc-mysql)

Build Tool: Gradle (Groovy)

1.2 기능 요구사항 (Functional Requirements)
채팅방 생성 및 입장 (Room Management):

사용자는 채팅방을 생성하거나 기존 방(roomId)에 입장할 수 있다. (N:N 구조)

실시간 메시지 전송 (Real-time Messaging):

사용자가 메시지를 보내면, 해당 방을 구독(Subscribe) 중인 모든 사용자에게 지연 없이 전달되어야 한다.

메시지 영속성 (Persistence):

모든 메시지는 MySQL에 저장되어야 한다.

서버 재시작 후에도 대화 내역이 유지되어야 한다.

입장/퇴장 알림:

새로운 사용자가 방에 들어오거나 나갈 때 시스템 메시지를 전송한다.

1.3 비기능 요구사항 (Non-Functional Requirements)
Non-blocking I/O: 모든 DB 작업과 네트워크 통신은 비동기/논블로킹으로 처리하여 적은 스레드로 동시 접속을 처리한다.

Scalability Foundation: 추후 외부 브로커(RabbitMQ 등) 연동이 용이하도록 Spring의 추상화된 메시징 인터페이스를 준수한다.