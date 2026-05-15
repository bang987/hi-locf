# hi-locf

`hi-locf`는 여신계약 기준의 `LOCF`와 `Provision(대손충당금)` 배치를 함께 학습할 수 있도록 만든 `Spring Boot + MyBatis + Oracle + WebSquare` 예제 프로젝트입니다.

핵심 방향은 다음과 같습니다.

- 공통 원천 데이터 기반 배치 처리
- Oracle 프로시저 + Java 서비스 혼합 구조
- LOCF와 Provision을 분리된 feature로 구성
- nexcore 스타일 JavaBean DTO/VO 방식 적용
- HTML / WebSquare 학습 화면 제공

## 1. 현재 기능

### LOCF
- 대상 계약 추출
- 약정 현금흐름 생성
- EIR 계산
- 상각수익/상각비용 계산
- 상각후원가 결과 저장
- 계약별 결과 / 요약 조회

### Provision
- 대상 계약 추출
- Stage / EAD / PD / LGD / ECL 산출
- 결과 요약 저장
- 계약별 결과 / 상품요약 / 세그먼트요약 조회

## 2. 기술 스택

- Java 17
- Spring Boot
- MyBatis
- Oracle XE
- H2(test)
- Maven
- HTML 학습 화면
- WebSquare 학습 XML

## 3. 프로젝트 구조

```text
src/main/java/com/hi/locf
├─ common
│  ├─ api
│  ├─ code
│  └─ exception
├─ config
└─ feature
   ├─ health
   ├─ locf
   │  ├─ controller
   │  ├─ dto
   │  ├─ entity
   │  ├─ mapper
   │  ├─ service
   │  └─ support
   └─ provision
      ├─ controller
      ├─ dto
      ├─ entity
      ├─ mapper
      ├─ service
      └─ support
```

## 4. DTO / VO 기준

현재 프로젝트의 응답 객체와 요청 객체는 nexcore 스타일에 맞춰 `record` 대신 JavaBean POJO를 사용합니다.

기준:
- 기본 생성자
- 전체 생성자
- getter / setter
- MyBatis 직접 매핑 가능

즉 `dto`는 실무형 `DTO/VO` 감각으로 이해하면 됩니다.

## 5. 주요 실행 URL

### HTML 학습 화면
- LOCF: `http://localhost:8080/hi-locf/locf-sample.html`
- Provision: `http://localhost:8080/hi-locf/provision-sample.html`

### Health
- `GET /hi-locf/api/v1/health`

### LOCF
- `POST /hi-locf/api/v1/locf/batches/run`
- `GET /hi-locf/api/v1/locf/batches`
- `GET /hi-locf/api/v1/locf/batches/{batchRunNo}/steps`
- `GET /hi-locf/api/v1/locf/source-contracts`
- `GET /hi-locf/api/v1/locf/contracts/{contractNo}`
- `GET /hi-locf/api/v1/locf/results/summary?baseDate=...`

### Provision
- `POST /hi-locf/api/v1/provision/batches/run`
- `GET /hi-locf/api/v1/provision/batches`
- `GET /hi-locf/api/v1/provision/batches/{batchRunNo}/steps`
- `GET /hi-locf/api/v1/provision/contracts/{contractNo}`
- `GET /hi-locf/api/v1/provision/results/summary?baseDate=...`
- `GET /hi-locf/api/v1/provision/results/segment-summary?baseDate=...`

## 6. DDL

- LOCF: [03_create_locf_tables.sql](D:/sts-5.1.1.RELEASE/workspace/hi-locf/src/main/resources/db/oracle/03_create_locf_tables.sql)
- Provision: [04_create_provision_tables.sql](D:/sts-5.1.1.RELEASE/workspace/hi-locf/src/main/resources/db/oracle/04_create_provision_tables.sql)

두 DDL 모두:
- drop 구문 포함
- 시퀀스 / 인덱스 포함
- 한글 테이블/컬럼 코멘트 포함

## 7. 학습용 문서

### LOCF
- 설계서: [locf-practical-design.md](D:/sts-5.1.1.RELEASE/workspace/hi-locf/docs/locf-practical-design.md)
- 구현 청사진: [locf-implementation-blueprint.md](D:/sts-5.1.1.RELEASE/workspace/hi-locf/docs/locf-implementation-blueprint.md)
- 전체 흐름: [locf-process-diagram.md](D:/sts-5.1.1.RELEASE/workspace/hi-locf/docs/locf-process-diagram.md)
- ERD: [locf-erd.md](D:/sts-5.1.1.RELEASE/workspace/hi-locf/docs/locf-erd.md)
- WebSquare 해설: [locf-websquare-walkthrough.md](D:/sts-5.1.1.RELEASE/workspace/hi-locf/docs/locf-websquare-walkthrough.md)

### Provision
- ERD: [provision-erd.md](D:/sts-5.1.1.RELEASE/workspace/hi-locf/docs/provision-erd.md)
- WebSquare 해설: [provision-websquare-walkthrough.md](D:/sts-5.1.1.RELEASE/workspace/hi-locf/docs/provision-websquare-walkthrough.md)

### 공통
- 템플릿 문서: [project-template.md](D:/sts-5.1.1.RELEASE/workspace/hi-locf/docs/project-template.md)

## 8. 실행 방법

### 1. Oracle DDL 반영
- `03_create_locf_tables.sql`
- `04_create_provision_tables.sql`

### 2. 애플리케이션 실행
- STS에서 Spring Boot App 실행
- 또는 `mvn spring-boot:run`

### 3. 화면 확인
- LOCF 화면
- Provision 화면

### 4. 테스트
- `mvn test`

## 9. 학습 순서 추천

1. `README`와 문서 링크를 먼저 본다.
2. LOCF DDL과 ERD를 본다.
3. `locf-sample.html`로 배치를 실행해본다.
4. `LocfBatchController -> LocfBatchServiceImpl -> Mapper/XML` 순서로 따라간다.
5. Provision DDL과 ERD를 본다.
6. `provision-sample.html`로 배치를 실행해본다.
7. `ProvisionBatchController -> ProvisionBatchServiceImpl -> 프로시저` 순서로 따라간다.
8. WebSquare XML과 해설 문서로 화면 연계 방식을 본다.

## 10. 한 줄 정리

이 프로젝트는 `원천 데이터 -> 배치 엔진 -> 결과 테이블 -> API -> HTML/WebSquare 화면` 흐름을 실무형으로 익히기 위한 학습용 프로젝트입니다.
