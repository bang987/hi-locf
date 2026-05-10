# HI-LOCF 학습형 템플릿

## 1. 전체 구조

```text
hi-locf
├─ pom.xml                              (비패키지, Maven 루트 설정)
├─ docs/
│  ├─ project-template.md              (비패키지, 프로젝트 가이드)
│  ├─ locf-practical-design.md         (비패키지, LOCF 실무형 설계서)
│  └─ locf-implementation-blueprint.md (비패키지, LOCF 구현 청사진)
├─ src/
│  ├─ main/
│  │  ├─ java/
│  │  │  └─ com/hi/locf/                (자바 패키지 루트)
│  │  │     ├─ HiLocfApplication.java
│  │  │     ├─ config/                  (패키지)
│  │  │     ├─ common/                  (패키지)
│  │  │     │  ├─ api/
│  │  │     │  ├─ code/
│  │  │     │  └─ exception/
│  │  │     └─ feature/                 (패키지)
│  │  │        ├─ health/controller/
│  │  │        └─ locf/
│  │  │           ├─ controller/
│  │  │           ├─ dto/
│  │  │           ├─ entity/
│  │  │           ├─ mapper/
│  │  │           └─ service/
│  │  └─ resources/
│  │     ├─ application.yml             (비패키지)
│  │     ├─ db/oracle/                  (비패키지, Oracle DDL)
│  │     ├─ mapper/                     (비패키지, MyBatis XML)
│  │     └─ static/
│  │        └─ websquare/               (비패키지, WebSquare XML 예시)
│  └─ test/
│     ├─ java/com/hi/locf/              (자바 패키지 루트)
│     └─ resources/                     (비패키지)
```

## 2. 설계 기준

- `feature` 하위로 업무 도메인을 모읍니다. 현재 기준 도메인은 `locf`.
- `common` 에는 공통 응답, 에러 코드, 예외 처리를 둡니다.
- `config` 에는 `@Configuration`, `@ConfigurationProperties`만 둡니다.
- 현재 구조는 `Oracle 원천 테이블 -> MyBatis LOCF 배치 계산 -> 결과 저장 -> WebSquare 조회` 흐름에 초점을 둡니다.
- 실무형 LOCF 확장 설계는 `docs/locf-practical-design.md`를 기준 문서로 사용합니다.
- 실제 개발 착수 기준 문서는 `docs/locf-implementation-blueprint.md`를 사용합니다.

## 3. WebSquare 연계 방식

### 권장 방식: 독립 배포형

- WebSquare UI와 Spring Boot API를 분리합니다.
- WebSquare는 WAS 또는 정적 웹서버에서 별도 배포합니다.
- Spring Boot는 `/hi-locf/api/v1/**` REST API만 제공합니다.
- 프론트와 백엔드 사이 통신은 `JSON` 기준으로 맞춥니다.
- 현재 기준 API는 LOCF 배치 실행과 계약 결과 조회입니다.

### 연계 흐름

```text
사용자 브라우저
  -> WebSquare 화면 서버 (예: http://localhost:8081)
  -> AJAX/Submission
  -> Spring Boot API (예: http://localhost:8080/hi-locf/api/v1/locf/contracts/LN-2026-000001)
  -> Oracle 저장 결과 조회 후 반환
```

### 백엔드에서 필요한 대응

- CORS 허용: `app.cors.allowed-origins`
- 표준 응답 포맷 유지: `ApiResponse<T>`
- 오류 응답 코드 표준화: `ErrorCode`, `GlobalExceptionHandler`
- 인증 필요 시 향후 `Spring Security + JWT/SSO` 추가

### WebSquare 요청 예시

```javascript
{
  "url": "http://localhost:8080/hi-locf/api/v1/locf/contracts/LN-2026-000001",
  "method": "GET",
  "headers": {
    "Content-Type": "application/json"
  }
}
```

### 응답 예시

```json
{
  "success": true,
  "data": {
    "contractNo": "LN-2026-000001",
    "latestBatchRunNo": "LOCF-20260510120000",
    "latestBatchStatus": "COMPLETED",
    "principal": 10000000.00,
    "initialCarryingAmount": 9870000.00,
    "annualEffectiveRate": 15.180599
  },
  "error": null,
  "timestamp": "2026-05-10T14:00:00"
}
```

## 4. 운영 시 추가 권장 항목

- 배치 실행 조건이 늘어나면 배치 파라미터 테이블을 추가합니다.
- 계약조건 변경 이력을 관리하려면 별도 도메인 테이블을 분리합니다.
- WebSquare와 계약이 자주 바뀌면 `docs/api-contract` 문서를 별도 폴더로 분리합니다.
