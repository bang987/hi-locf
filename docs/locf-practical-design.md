# HI-LOCF 실무형 설계서

## 1. 문서 목적

이 문서는 `Spring Boot + MyBatis + Oracle XE + WebSquare` 환경에서 저축은행 여신계약의 `LOCF(Loan Cash Flow)` 산출 프로젝트를 실무형으로 설계할 때의 기준안을 정리한 문서입니다.

현재 프로젝트의 학습용 샘플을 다음 방향으로 확장하는 것을 전제로 합니다.

- 공통 원천 데이터 관리
- 계약 현금흐름 생성
- EIR 계산
- 상각수익/상각비용 계산
- 상각후원가 산출
- 배치 실행 이력과 결과 저장
- WebSquare 조회 화면 연계

## 2. 업무 목표

LOCF 엔진의 목표는 단순 상환 스케줄 조회가 아니라, 회계 기준의 유효이자율법에 따라 계약별 장부가와 상각금액을 산출하는 것입니다.

핵심 산출물은 다음과 같습니다.

- 계약별 약정 현금흐름
- 계약별 EIR
- 회차별 약정이자, 유효이자수익, 수수료/직접원가 상각액
- 회차별 상각후원가 잔액
- 기준일자별 LOCF 결과 이력

## 3. 상위 구조

```text
공통 원천 데이터

  -> LOCF 대상 추출
  -> 계약 현금흐름 생성
  -> EIR 계산
  -> 상각수익/상각비용 계산
  -> 상각후원가 산출
  -> 결과 저장
  -> WebSquare 조회
```

역할 분리는 다음과 같이 둡니다.

- `Oracle`
  대량 계약, 조인, 집합 연산, 배치 적재
- `MyBatis`
  Oracle SQL과 프로시저 호출 계층
- `Spring Service`
  배치 실행 순서, 상태 관리, 예외 처리, 조회용 응답 조립
- `WebSquare`
  결과 조회, 배치 실행 요청, 상세 화면 바인딩

## 4. 공통 원천 데이터

LOCF 엔진은 다음 원천 데이터를 기준으로 움직입니다.

### 4.1 계약정보

- 계약번호
- 상품코드
- 실행일
- 만기일
- 상환방식
- 통화
- 원금
- 취급수수료
- 직접원가
- 상태

예시 테이블:

- `LOAN_CONTRACT`

### 4.2 금리

- 약정금리
- 변동/고정 여부
- 금리변경일
- 금리유형
- 가산금리/기준금리

예시 테이블:

- `LOAN_RATE`
- 또는 계약 테이블 내 금리 컬럼

### 4.3 상환스케줄

- 회차
- 약정상환일
- 약정원금
- 약정이자
- 만기일시상환 여부
- 원리금균등/원금균등 구분

예시 테이블:

- `LOAN_REPAYMENT_SCHEDULE`

### 4.4 잔액

- 기준일 현재 원금잔액
- 미수이자
- 선수수익/선급비용 관련 잔액
- 상각후원가 기준 잔액

예시 테이블:

- `LOAN_BALANCE`

### 4.5 고객정보

- 고객번호
- 고객명
- 개인/법인 여부
- 내부등급
- 세그먼트
- 관계사 여부

예시 테이블:

- `CUSTOMER_MASTER`

## 5. LOCF 엔진 처리 구조

LOCF 엔진은 아래 4단계로 설계합니다.

### 5.1 계약 현금흐름 생성

목적:

- 원천 계약과 상환조건을 이용해 계약별 약정 현금흐름을 생성

주요 처리:

- 상환방식별 회차 생성
- 약정 납입액 계산
- 회차별 원금/이자 분해
- 만기풍선, 거치기간, 비정형 스케줄 반영

주요 입력:

- `LOAN_CONTRACT`
- `LOAN_RATE`
- `LOAN_REPAYMENT_SCHEDULE`

주요 출력:

- `LOCF_CASHFLOW_BASE`

저장 컬럼 예:

- `base_date`
- `batch_run_no`
- `contract_id`
- `contract_no`
- `installment_no`
- `payment_date`
- `scheduled_payment_amt`
- `scheduled_principal_amt`
- `scheduled_interest_amt`

### 5.2 EIR 계산

목적:

- 최초 인식 장부가와 미래 계약 현금흐름을 기준으로 계약별 유효이자율을 계산

주요 처리:

- 최초 장부가 산출
  - `원금 - 취급수수료 + 직접원가`
- 월 EIR 또는 일 EIR 계산
- 연 EIR 환산

주요 입력:

- `LOAN_CONTRACT`
- `LOCF_CASHFLOW_BASE`

주요 출력:

- `LOCF_EIR_RESULT`

저장 컬럼 예:

- `base_date`
- `batch_run_no`
- `contract_id`
- `initial_carrying_amount`
- `monthly_eir`
- `annual_eir`

### 5.3 상각수익/상각비용 계산

목적:

- 약정이자와 유효이자수익의 차이를 이용해 회차별 상각액을 계산

주요 처리:

- 회차별 유효이자수익 계산
- 수수료 상각액 계산
- 직접원가 상각액 계산
- 순상각액 계산

주요 입력:

- `LOCF_CASHFLOW_BASE`
- `LOCF_EIR_RESULT`

주요 출력:

- `LOCF_AMORTIZATION_DTL`

저장 컬럼 예:

- `installment_no`
- `opening_principal_balance`
- `opening_carrying_amount`
- `effective_interest_revenue`
- `fee_amortization_amt`
- `cost_amortization_amt`
- `net_amortization_amt`

### 5.4 상각후원가 산출

목적:

- 회차별 상각 결과를 누적해 기말 상각후원가 잔액을 산출

주요 처리:

- 기초 장부가에서 회차별 원금회수와 상각 반영
- 기말 장부가 계산
- 만기 시 장부가 수렴 검증

주요 입력:

- `LOCF_AMORTIZATION_DTL`

주요 출력:

- `LOCF_RESULT_HDR`
- `LOCF_RESULT_DTL`

헤더 저장 컬럼 예:

- `base_date`
- `batch_run_no`
- `contract_id`
- `contract_no`
- `initial_carrying_amount`
- `annual_eir`
- `final_carrying_amount`
- `total_fee_amortization_amt`
- `total_cost_amortization_amt`

상세 저장 컬럼 예:

- `installment_no`
- `payment_date`
- `scheduled_payment_amt`
- `scheduled_principal_amt`
- `scheduled_interest_amt`
- `effective_interest_revenue`
- `net_amortization_amt`
- `closing_carrying_amount`

## 6. 권장 테이블 설계

### 6.1 원천 테이블

- `LOAN_CONTRACT`
- `LOAN_RATE`
- `LOAN_REPAYMENT_SCHEDULE`
- `LOAN_BALANCE`
- `CUSTOMER_MASTER`

### 6.2 배치 제어 테이블

- `LOCF_BATCH_EXECUTION`
- `LOCF_BATCH_STEP_EXECUTION`
- `LOCF_BATCH_ERROR_LOG`

### 6.3 중간 산출 테이블

- `LOCF_TARGET_CONTRACT`
- `LOCF_CASHFLOW_BASE`
- `LOCF_EIR_RESULT`
- `LOCF_AMORTIZATION_DTL`

### 6.4 최종 결과 테이블

- `LOCF_RESULT_HDR`
- `LOCF_RESULT_DTL`
- `LOCF_RESULT_SUMMARY`

## 7. 배치 처리 단계

### Step 1. 배치 실행 등록

Java Service 역할:

- 기준일자 검증
- 실행번호 생성
- 실행 이력 상태 `RUNNING` 등록

테이블:

- `LOCF_BATCH_EXECUTION`

### Step 2. 대상 계약 추출

Mapper SQL 역할:

- 기준일 현재 유효 계약 추출
- 종료/해지/제외 대상 제거
- 상품/상태 기준 필터링

테이블:

- 입력: `LOAN_CONTRACT`, `LOAN_BALANCE`
- 출력: `LOCF_TARGET_CONTRACT`

### Step 3. 계약 현금흐름 생성

Mapper SQL 또는 프로시저 역할:

- 약정 스케줄 적재
- 필요 시 회차 생성 로직 수행

테이블:

- 입력: `LOAN_REPAYMENT_SCHEDULE`
- 출력: `LOCF_CASHFLOW_BASE`

### Step 4. EIR 계산

처리 기준:

- 건별 계산 성격이 강하므로, 구현 초기에는 Java 계산 서비스 사용 가능
- 대량 처리 안정화 이후에는 Oracle 함수/프로시저로 이전 가능

테이블:

- 입력: `LOCF_CASHFLOW_BASE`
- 출력: `LOCF_EIR_RESULT`

### Step 5. 상각수익/상각비용 계산

처리 기준:

- 회차별 상세 산출
- 계약별 누적 계산
- 결과 상세 저장

테이블:

- 입력: `LOCF_CASHFLOW_BASE`, `LOCF_EIR_RESULT`
- 출력: `LOCF_AMORTIZATION_DTL`

### Step 6. 상각후원가 결과 적재

Mapper SQL 역할:

- 상세 결과를 기준으로 헤더 요약 적재
- 상품/고객/기준일 요약 집계

테이블:

- `LOCF_RESULT_HDR`
- `LOCF_RESULT_DTL`
- `LOCF_RESULT_SUMMARY`

### Step 7. 배치 종료 처리

Java Service 역할:

- 처리 건수 업데이트
- 성공/실패 상태 반영
- 오류 로그 기록

## 8. Spring Boot 패키지 설계

```text
com.hi.locf.feature.locf
├─ controller
│  ├─ LocfBatchController
│  └─ LocfQueryController
├─ dto
│  ├─ LocfBatchRunRequest
│  ├─ LocfBatchRunResponse
│  ├─ LocfResultSearchRequest
│  ├─ LocfResultSummaryResponse
│  └─ LocfResultDetailResponse
├─ entity
│  ├─ LocfBatchExecution
│  ├─ LocfTargetContract
│  ├─ LocfCashflowBase
│  ├─ LocfEirResult
│  ├─ LocfResultHeader
│  └─ LocfResultDetail
├─ mapper
│  ├─ LocfBatchControlMapper
│  ├─ LocfTargetContractMapper
│  ├─ LocfCashflowMapper
│  ├─ LocfEirMapper
│  ├─ LocfAmortizationMapper
│  └─ LocfQueryMapper
└─ service
   ├─ LocfBatchService
   ├─ LocfBatchServiceImpl
   ├─ LocfScheduleService
   ├─ LocfEirService
   └─ LocfQueryService
```

## 9. Mapper 역할 기준

### Mapper에서 처리할 것

- 대상 계약 대량 추출
- 대량 `INSERT INTO ... SELECT`
- 요약 집계
- 배치 상태 갱신
- 조회 화면용 조인 SQL

### Java Service에서 처리할 것

- 배치 단계 순서 제어
- 건별 EIR 계산 엔진 호출
- 예외 처리
- 재실행 정책
- API 응답 조립

## 10. API 설계 예시

### 배치 실행

- `POST /hi-locf/api/v1/locf/batches/run`

역할:

- 기준일자 기준 LOCF 배치 실행

### 실행 이력 조회

- `GET /hi-locf/api/v1/locf/batches`

역할:

- 기준일자/상태별 실행 목록 조회

### 계약별 결과 조회

- `GET /hi-locf/api/v1/locf/contracts/{contractNo}`

역할:

- 계약별 최신 LOCF 결과 헤더 + 회차 상세 조회

### 요약 결과 조회

- `GET /hi-locf/api/v1/locf/results/summary?baseDate=2026-05-10`

역할:

- 기준일자별 계약건수, 상각후원가, 유효이자수익 합계 조회

## 11. WebSquare 연계 기준

WebSquare는 계산 엔진이 아니라 조회/실행 UI로 둡니다.

주요 화면:

- LOCF 배치 실행 화면
- 실행 이력 조회 화면
- 계약별 LOCF 결과 조회 화면
- 회차별 상각 상세 그리드 화면
- 기준일자별 요약 화면

데이터셋 예시:

- `dmBatchSearch`
- `dmContractSearch`
- `dlBatchHistory`
- `dmResultHeader`
- `dlResultDetail`
- `dlSummary`

## 12. 성능 설계 기준

- 계약 수가 커질수록 대상 추출과 집계는 Oracle 집합 연산으로 처리
- 결과 테이블은 `base_date` 또는 `batch_run_no` 기준 조회를 최적화
- 필요 시 배치 대상 테이블과 결과 상세 테이블은 파티셔닝 검토
- 대량 insert는 건건 insert보다 `INSERT SELECT` 우선
- 배치 재실행은 `base_date + batch_type` 단위 정책 명확화

## 13. 검증 포인트

### 정합성 검증

- 최초 장부가 = `원금 - 취급수수료 + 직접원가`
- 회차별 약정현금흐름 합계 검증
- 만기 시 장부가 수렴 여부 검증
- 총 상각액 = 총 수수료 - 총 직접원가 검증

### 운영 검증

- 전일 대비 장부가 증감
- 상품별 EIR 분포 이상치
- 상각액 음수/과대값 점검
- 재실행 시 중복 적재 여부 점검

## 14. 향후 확장 방향

- 변동금리 상품 지원
- 중도상환/재약정 반영
- 연체 상태 계약 분기 처리
- 회계 결산월 기준 요약 리포트
- 충당금 엔진과의 연계
  - LOCF 결과 중 `상각후원가`, `EIR`, `계약 현금흐름` 제공

## 15. 결론

LOCF 프로젝트는 단순 상환 스케줄 계산기가 아니라, 공통 원천 데이터를 기반으로 계약 현금흐름, EIR, 상각금액, 상각후원가를 단계별로 산출하는 배치형 엔진으로 설계해야 합니다.

현재 프로젝트 환경에서는 다음 원칙이 적합합니다.

- 대량 계약 추출과 집계는 `Oracle + MyBatis`
- 배치 순서와 상태 관리는 `Spring Service`
- 결과 조회와 실행은 `WebSquare`
- 원천, 중간, 최종 결과 테이블을 분리해 검증 가능하게 설계
