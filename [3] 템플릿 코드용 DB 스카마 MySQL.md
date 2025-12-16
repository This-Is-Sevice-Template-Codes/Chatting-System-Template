
---

## 📝 README.md 추가 및 보완 사항

아래 내용은 기존 기술 명세서 뒤에 이어붙이거나, 별도의 **[Project Strategy & scenarios]** 챕터로 구성하면 좋습니다.

###  보안 및 안정성 설계 (Security & Stability)

**주요 포인트:** 단순한 채팅 기능을 넘어, 엔터프라이즈 환경에서도 안전하게 사용할 수 있는 보안 및 예외 처리 전략을 내재화했습니다.

* **[S-01] WebSocket 보안 (JWT 인증):**
    * HTTP Handshake 단계가 아닌, **STOMP CONNECT 프레임 헤더**에 JWT 토큰을 실어 인증을 수행합니다.
    * `ChannelInterceptor`를 통해 유효하지 않은 토큰 접근을 원천 차단합니다.
* **[S-02] 동시성 제어 (Concurrency Control):**
    * `chat_member` 테이블에 Unique Key가 없는 구조적 특성상, **Application Level Lock** (또는 `Redis Distributed Lock` 추후 도입)을 통해 따닥(Double Click)으로 인한 중복 입장 이슈를 방어합니다.
* **[S-03] 글로벌 예외 처리 (Global Error Handling):**
    * 비동기 환경(WebFlux)에서 자칫 놓치기 쉬운 예외를 `GlobalExceptionHandler`와 `STOMP Error Frame`으로 캡처하여 클라이언트에게 명확한 에러 원인(예: "이미 종료된 방입니다")을 전달합니다.

###  핵심 시나리오 (Key Scenarios)

시스템의 흐름을 쉽게 이해할 수 있도록 구체적인 사용 예시

### Scenario A: 그룹 채팅방 생성 및 초대 (The "Viral" Flow)
1.  **User A**가 '샘플' 방을 생성하며 **User B, C**를 초대.
2.  **Server**는 `chat_room` 생성 후, `chat_member`에 A, B, C를 `JOINED` 상태로 Insert.
3.  **User A**의 화면에는 즉시 채팅방이 열림.
4.  **User B, C**가 접속 중이라면, 실시간으로 채팅방 목록에 '샘플' 방이 **Push** 되어 나타남.

### Scenario B: 퇴장 후 재입장 (The "Soft Delete" Flow)
1.  **User B**가 '샘플' 방에서 **퇴장**.
    * DB: `chat_member`에서 B의 상태를 `LEFT`로 업데이트 (Log 보존).
    * SysMsg: "B님이 나갔습니다." 메시지 발송.
2.  **User B**가 마음이 바뀌어 **재입장** 시도.
    * Server: `chat_member` 조회 → `status='LEFT'` 기록 확인.
    * Action: 새로운 Row를 Insert하지 않고, 기존 Row의 상태를 `JOINED`로, `joined_at`을 현재 시간으로 **Update**. (데이터 효율성 및 이력 연결)

---

###  확장 로드맵 (Expansion Roadmap)

MVP 이후, 사용자가 10만, 100만 명으로 늘어났을 때의 기술적 대응 전략입니다.

| 단계 | 목표 | 주요 기술 도입 | 비즈니스 가치 |
| :--- | :--- | :--- | :--- |
| **Phase 1 (Current)** | **MVP & Validation** | **MySQL (Single), In-Memory Broker** | 최소 비용으로 시장성 검증 및 핵심 기능 구현 |
| **Phase 2** | **Stability** | **RabbitMQ, Redis Cache** | 메시지 유실 방지 및 채팅 목록 조회 속도 10배 향상 |
| **Phase 3** | **Hyper-Scale** | **MongoDB (Chat Log), Sharding** | 카카오톡 수준의 대용량 트래픽 처리 및 무한 스크롤 성능 보장 |

---

### 참고사항

* **Why WebFlux?**
    * 전통적인 Servlet 방식은 동시 접속자 1만 명 시 1만 개의 스레드를 생성하여 서버가 다운될 위험이 큽니다. WebFlux는 소수의 스레드로 수만 명의 연결을 유지하므로 **하드웨어 비용을 1/5 수준으로 절감**합니다.
* **Why Soft Delete?**
    * 최근 IT 트렌드는 데이터가 곧 자산입니다. 사용자의 입장/퇴장 패턴, 머무른 시간 등을 추후 **데이터 분석 및 마케팅(Retention Check)**에 활용하기 위해 물리적 삭제를 배제했습니다.

---