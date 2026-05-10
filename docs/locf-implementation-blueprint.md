# HI-LOCF 구현 청사진

## 1. 문서 목적

이 문서는 [locf-practical-design.md](D:\sts-5.1.1.RELEASE\workspace\hi-locf\docs\locf-practical-design.md)의 설계를 실제 개발 착수 수준으로 구체화한 구현 청사진입니다.

목표는 다음 4가지를 바로 만들 수 있게 하는 것입니다.

- Oracle DDL
- Spring Boot 패키지/클래스 구조
- MyBatis Mapper 인터페이스와 XML 역할
- WebSquare 조회/실행 화면 연계 포인트

## 2. 구현 범위

이번 단계의 LOCF 구현 범위는 아래로 한정합니다.

- 원리금균등/원금균등/만기일시상환 상품 지원
- 계약별 약정 현금흐름 생성
- 월 EIR 계산
- 회차별 유효이자수익, 수수료/직접원가 상각액 산출
- 상각후원가 산출
- 기준일자 단위 배치 실행
- 계약번호 기준 결과 조회
- 기준일자 기준 요약 조회

제외 범위:

- 변동금리 재산출
- 중도상환 재계산
- 연체/부도 반영 로직
- 충당금 엔진 연계

## 3. 권장 Oracle 테이블

### 3.1 원천 테이블

이미 다른 시스템에서 들어온다고 가정합니다.

#### `LOAN_CONTRACT`

| 컬럼명 | 타입 | 설명 |
|---|---|---|
| CONTRACT_ID | NUMBER(18) | PK |
| CONTRACT_NO | VARCHAR2(30) | 계약번호 |
| CUSTOMER_ID | NUMBER(18) | 고객 ID |
| PRODUCT_CODE | VARCHAR2(30) | 상품코드 |
| LOAN_TYPE_CODE | VARCHAR2(20) | 대출유형 |
| EXECUTION_DATE | DATE | 실행일 |
| MATURITY_DATE | DATE | 만기일 |
| REPAYMENT_TYPE | VARCHAR2(20) | 상환방식 |
| PRINCIPAL_AMT | NUMBER(18,2) | 원금 |
| FEE_AMT | NUMBER(18,2) | 취급수수료 |
| DIRECT_COST_AMT | NUMBER(18,2) | 직접원가 |
| STATUS_CODE | VARCHAR2(20) | 계약상태 |
| CREATED_AT | DATE | 생성일시 |
| UPDATED_AT | DATE | 수정일시 |

#### `LOAN_RATE`

| 컬럼명 | 타입 | 설명 |
|---|---|---|
| RATE_ID | NUMBER(18) | PK |
| CONTRACT_ID | NUMBER(18) | 계약 ID |
| RATE_TYPE | VARCHAR2(20) | FIXED / FLOATING |
| ANNUAL_NOMINAL_RATE | NUMBER(12,6) | 연 약정금리 |
| EFFECTIVE_FROM | DATE | 적용 시작일 |
| EFFECTIVE_TO | DATE | 적용 종료일 |

#### `LOAN_REPAYMENT_SCHEDULE`

| 컬럼명 | 타입 | 설명 |
|---|---|---|
| SCHEDULE_ID | NUMBER(18) | PK |
| CONTRACT_ID | NUMBER(18) | 계약 ID |
| INSTALLMENT_NO | NUMBER(6) | 회차 |
| PAYMENT_DATE | DATE | 약정상환일 |
| SCHEDULED_PAYMENT_AMT | NUMBER(18,2) | 약정납입액 |
| SCHEDULED_PRINCIPAL_AMT | NUMBER(18,2) | 약정원금 |
| SCHEDULED_INTEREST_AMT | NUMBER(18,2) | 약정이자 |

#### `LOAN_BALANCE`

| 컬럼명 | 타입 | 설명 |
|---|---|---|
| BALANCE_ID | NUMBER(18) | PK |
| CONTRACT_ID | NUMBER(18) | 계약 ID |
| BASE_DATE | DATE | 기준일자 |
| OUTSTANDING_PRINCIPAL_AMT | NUMBER(18,2) | 원금잔액 |
| ACCRUED_INTEREST_AMT | NUMBER(18,2) | 미수이자 |
| STATUS_CODE | VARCHAR2(20) | 잔액상태 |

#### `CUSTOMER_MASTER`

| 컬럼명 | 타입 | 설명 |
|---|---|---|
| CUSTOMER_ID | NUMBER(18) | PK |
| CUSTOMER_NO | VARCHAR2(30) | 고객번호 |
| CUSTOMER_NAME | VARCHAR2(100) | 고객명 |
| CUSTOMER_TYPE | VARCHAR2(20) | 개인/법인 |
| SEGMENT_CODE | VARCHAR2(20) | 세그먼트 |

### 3.2 배치 제어 테이블

#### `LOCF_BATCH_EXECUTION`

| 컬럼명 | 타입 | 설명 |
|---|---|---|
| BATCH_EXECUTION_ID | NUMBER(18) | PK |
| BATCH_RUN_NO | VARCHAR2(40) | 실행번호 |
| BASE_DATE | DATE | 기준일자 |
| BATCH_TYPE | VARCHAR2(20) | DAILY / MANUAL |
| STATUS_CODE | VARCHAR2(20) | RUNNING / COMPLETED / FAILED |
| STARTED_AT | DATE | 시작일시 |
| FINISHED_AT | DATE | 종료일시 |
| TARGET_COUNT | NUMBER(12) | 대상건수 |
| PROCESSED_COUNT | NUMBER(12) | 처리건수 |
| ERROR_COUNT | NUMBER(12) | 오류건수 |
| ERROR_MESSAGE | VARCHAR2(1000) | 대표오류 |

#### `LOCF_BATCH_STEP_EXECUTION`

| 컬럼명 | 타입 | 설명 |
|---|---|---|
| STEP_EXECUTION_ID | NUMBER(18) | PK |
| BATCH_EXECUTION_ID | NUMBER(18) | 배치 실행 ID |
| STEP_NAME | VARCHAR2(50) | 단계명 |
| STATUS_CODE | VARCHAR2(20) | RUNNING / COMPLETED / FAILED |
| STARTED_AT | DATE | 시작일시 |
| FINISHED_AT | DATE | 종료일시 |
| PROCESSED_COUNT | NUMBER(12) | 단계 처리건수 |
| ERROR_MESSAGE | VARCHAR2(1000) | 오류메시지 |

### 3.3 중간 산출 테이블

#### `LOCF_TARGET_CONTRACT`

배치 기준일에 실제 산출 대상이 되는 계약 집합입니다.

| 컬럼명 | 타입 | 설명 |
|---|---|---|
| TARGET_ID | NUMBER(18) | PK |
| BATCH_EXECUTION_ID | NUMBER(18) | 배치 실행 ID |
| CONTRACT_ID | NUMBER(18) | 계약 ID |
| CONTRACT_NO | VARCHAR2(30) | 계약번호 |
| CUSTOMER_ID | NUMBER(18) | 고객 ID |
| PRODUCT_CODE | VARCHAR2(30) | 상품코드 |
| REPAYMENT_TYPE | VARCHAR2(20) | 상환방식 |
| PRINCIPAL_AMT | NUMBER(18,2) | 원금 |
| FEE_AMT | NUMBER(18,2) | 수수료 |
| DIRECT_COST_AMT | NUMBER(18,2) | 직접원가 |
| ANNUAL_NOMINAL_RATE | NUMBER(12,6) | 연 약정금리 |
| EXECUTION_DATE | DATE | 실행일 |
| MATURITY_DATE | DATE | 만기일 |
| TERM_IN_MONTHS | NUMBER(6) | 총개월수 |

#### `LOCF_CASHFLOW_BASE`

| 컬럼명 | 타입 | 설명 |
|---|---|---|
| CASHFLOW_BASE_ID | NUMBER(18) | PK |
| BATCH_EXECUTION_ID | NUMBER(18) | 배치 실행 ID |
| CONTRACT_ID | NUMBER(18) | 계약 ID |
| CONTRACT_NO | VARCHAR2(30) | 계약번호 |
| INSTALLMENT_NO | NUMBER(6) | 회차 |
| PAYMENT_DATE | DATE | 상환일 |
| SCHEDULED_PAYMENT_AMT | NUMBER(18,2) | 약정납입액 |
| SCHEDULED_PRINCIPAL_AMT | NUMBER(18,2) | 약정원금 |
| SCHEDULED_INTEREST_AMT | NUMBER(18,2) | 약정이자 |

#### `LOCF_EIR_RESULT`

| 컬럼명 | 타입 | 설명 |
|---|---|---|
| EIR_RESULT_ID | NUMBER(18) | PK |
| BATCH_EXECUTION_ID | NUMBER(18) | 배치 실행 ID |
| CONTRACT_ID | NUMBER(18) | 계약 ID |
| CONTRACT_NO | VARCHAR2(30) | 계약번호 |
| INITIAL_CARRYING_AMOUNT | NUMBER(18,2) | 최초장부가 |
| MONTHLY_EIR | NUMBER(18,10) | 월 EIR |
| ANNUAL_EIR | NUMBER(18,10) | 연 EIR |

#### `LOCF_AMORTIZATION_DTL`

| 컬럼명 | 타입 | 설명 |
|---|---|---|
| AMORTIZATION_DTL_ID | NUMBER(18) | PK |
| BATCH_EXECUTION_ID | NUMBER(18) | 배치 실행 ID |
| CONTRACT_ID | NUMBER(18) | 계약 ID |
| CONTRACT_NO | VARCHAR2(30) | 계약번호 |
| INSTALLMENT_NO | NUMBER(6) | 회차 |
| PAYMENT_DATE | DATE | 상환일 |
| OPENING_PRINCIPAL_BAL | NUMBER(18,2) | 기초원금 |
| OPENING_CARRYING_AMT | NUMBER(18,2) | 기초장부가 |
| SCHEDULED_PAYMENT_AMT | NUMBER(18,2) | 약정납입액 |
| SCHEDULED_PRINCIPAL_AMT | NUMBER(18,2) | 약정원금 |
| SCHEDULED_INTEREST_AMT | NUMBER(18,2) | 약정이자 |
| EFFECTIVE_INTEREST_REVENUE | NUMBER(18,2) | 유효이자수익 |
| FEE_AMORTIZATION_AMT | NUMBER(18,2) | 수수료 상각액 |
| COST_AMORTIZATION_AMT | NUMBER(18,2) | 직접원가 상각액 |
| NET_AMORTIZATION_AMT | NUMBER(18,2) | 순상각액 |
| CLOSING_PRINCIPAL_BAL | NUMBER(18,2) | 기말원금 |
| CLOSING_CARRYING_AMT | NUMBER(18,2) | 기말장부가 |

### 3.4 최종 결과 테이블

#### `LOCF_RESULT_HDR`

| 컬럼명 | 타입 | 설명 |
|---|---|---|
| RESULT_HDR_ID | NUMBER(18) | PK |
| BATCH_EXECUTION_ID | NUMBER(18) | 배치 실행 ID |
| BASE_DATE | DATE | 기준일자 |
| CONTRACT_ID | NUMBER(18) | 계약 ID |
| CONTRACT_NO | VARCHAR2(30) | 계약번호 |
| CUSTOMER_ID | NUMBER(18) | 고객 ID |
| PRODUCT_CODE | VARCHAR2(30) | 상품코드 |
| INITIAL_CARRYING_AMOUNT | NUMBER(18,2) | 최초장부가 |
| ANNUAL_EIR | NUMBER(18,10) | 연 EIR |
| FINAL_CARRYING_AMOUNT | NUMBER(18,2) | 최종장부가 |
| TOTAL_EFFECTIVE_INTEREST_REV | NUMBER(18,2) | 총 유효이자수익 |
| TOTAL_FEE_AMORTIZATION_AMT | NUMBER(18,2) | 총 수수료 상각액 |
| TOTAL_COST_AMORTIZATION_AMT | NUMBER(18,2) | 총 직접원가 상각액 |

#### `LOCF_RESULT_DTL`

실무에서는 `LOCF_AMORTIZATION_DTL`을 최종 상세로 그대로 겸용할 수도 있습니다. 운영/조회 분리를 원하면 별도 테이블로 둡니다.

#### `LOCF_RESULT_SUMMARY`

| 컬럼명 | 타입 | 설명 |
|---|---|---|
| SUMMARY_ID | NUMBER(18) | PK |
| BATCH_EXECUTION_ID | NUMBER(18) | 배치 실행 ID |
| BASE_DATE | DATE | 기준일자 |
| PRODUCT_CODE | VARCHAR2(30) | 상품코드 |
| CONTRACT_COUNT | NUMBER(12) | 계약건수 |
| TOTAL_INITIAL_CARRYING_AMT | NUMBER(18,2) | 최초장부가 합계 |
| TOTAL_FINAL_CARRYING_AMT | NUMBER(18,2) | 최종장부가 합계 |
| TOTAL_EFFECTIVE_INTEREST_REV | NUMBER(18,2) | 유효이자수익 합계 |

## 4. 시퀀스와 인덱스

### 시퀀스

- `SQ_LOCF_BATCH_EXECUTION`
- `SQ_LOCF_BATCH_STEP_EXECUTION`
- `SQ_LOCF_TARGET_CONTRACT`
- `SQ_LOCF_CASHFLOW_BASE`
- `SQ_LOCF_EIR_RESULT`
- `SQ_LOCF_AMORTIZATION_DTL`
- `SQ_LOCF_RESULT_HDR`
- `SQ_LOCF_RESULT_SUMMARY`

### 필수 인덱스

- `LOCF_BATCH_EXECUTION(BATCH_RUN_NO)`
- `LOCF_BATCH_EXECUTION(BASE_DATE, STATUS_CODE)`
- `LOCF_TARGET_CONTRACT(BATCH_EXECUTION_ID, CONTRACT_ID)`
- `LOCF_CASHFLOW_BASE(BATCH_EXECUTION_ID, CONTRACT_ID, INSTALLMENT_NO)`
- `LOCF_EIR_RESULT(BATCH_EXECUTION_ID, CONTRACT_ID)`
- `LOCF_AMORTIZATION_DTL(BATCH_EXECUTION_ID, CONTRACT_ID, INSTALLMENT_NO)`
- `LOCF_RESULT_HDR(BASE_DATE, CONTRACT_NO)`
- `LOCF_RESULT_SUMMARY(BASE_DATE, PRODUCT_CODE)`

## 5. Spring Boot 패키지 구조

```text
src/main/java/com/hi/locf/feature/locf
├─ controller
│  ├─ LocfBatchController.java
│  └─ LocfQueryController.java
├─ dto
│  ├─ LocfBatchRunRequest.java
│  ├─ LocfBatchRunResponse.java
│  ├─ LocfBatchHistoryItemResponse.java
│  ├─ LocfContractResultResponse.java
│  ├─ LocfContractResultHeaderResponse.java
│  ├─ LocfContractResultDetailRow.java
│  ├─ LocfSummarySearchRequest.java
│  └─ LocfSummaryItemResponse.java
├─ entity
│  ├─ LocfBatchExecution.java
│  ├─ LocfBatchStepExecution.java
│  ├─ LocfTargetContract.java
│  ├─ LocfCashflowBase.java
│  ├─ LocfEirResult.java
│  ├─ LocfAmortizationDetail.java
│  ├─ LocfResultHeader.java
│  └─ LocfResultSummary.java
├─ mapper
│  ├─ LocfBatchControlMapper.java
│  ├─ LocfTargetContractMapper.java
│  ├─ LocfCashflowMapper.java
│  ├─ LocfEirMapper.java
│  ├─ LocfAmortizationMapper.java
│  └─ LocfQueryMapper.java
└─ service
   ├─ LocfBatchService.java
   ├─ LocfBatchServiceImpl.java
   ├─ LocfCashflowService.java
   ├─ LocfCashflowServiceImpl.java
   ├─ LocfEirService.java
   ├─ LocfEirServiceImpl.java
   ├─ LocfAmortizationService.java
   ├─ LocfAmortizationServiceImpl.java
   ├─ LocfResultAggregationService.java
   ├─ LocfResultAggregationServiceImpl.java
   ├─ LocfQueryService.java
   └─ LocfQueryServiceImpl.java
```

## 6. Controller 설계

### `LocfBatchController`

역할:

- 배치 실행 요청
- 실행 이력 목록 조회

API:

- `POST /hi-locf/api/v1/locf/batches/run`
- `GET /hi-locf/api/v1/locf/batches`

### `LocfQueryController`

역할:

- 계약번호 기준 상세 조회
- 기준일자 기준 요약 조회

API:

- `GET /hi-locf/api/v1/locf/contracts/{contractNo}`
- `GET /hi-locf/api/v1/locf/results/summary`

## 7. Service 설계

### `LocfBatchService`

주요 메서드:

- `LocfBatchRunResponse runBatch(LocfBatchRunRequest request)`
- `List<LocfBatchHistoryItemResponse> getBatchHistory(LocalDate baseDate)`

### `LocfBatchServiceImpl`

권장 처리 순서:

1. 배치 실행 등록
2. 대상 계약 생성
3. 현금흐름 생성
4. EIR 계산
5. 상각 상세 생성
6. 결과 헤더/요약 생성
7. 배치 성공/실패 종료

의사코드:

```java
runBatch(request) {
    execution = batchControlMapper.insertBatchExecution(...)
    stepStart("TARGET")
    targetContractMapper.insertTargetContracts(executionId, baseDate)
    stepComplete("TARGET")

    stepStart("CASHFLOW")
    cashflowMapper.insertCashflowBase(executionId)
    stepComplete("CASHFLOW")

    stepStart("EIR")
    eirService.calculateAndStoreEir(executionId)
    stepComplete("EIR")

    stepStart("AMORTIZATION")
    amortizationService.calculateAndStoreAmortization(executionId)
    stepComplete("AMORTIZATION")

    stepStart("RESULT")
    amortizationMapper.insertResultHeaders(executionId)
    amortizationMapper.insertResultSummary(executionId)
    stepComplete("RESULT")

    batchControlMapper.completeBatchExecution(...)
}
```

### `LocfCashflowService`

역할:

- 상환방식별 현금흐름 생성 규칙 제공
- 초기 버전에서는 Java 계산 후 `LOCF_CASHFLOW_BASE` 적재 가능
- 이후에는 SQL/프로시저 전환 가능

메서드:

- `List<LocfCashflowBase> generateCashflow(LocfTargetContract contract)`

### `LocfEirService`

역할:

- 계약별 EIR 계산

메서드:

- `LocfEirResult calculateEir(LocfTargetContract contract, List<LocfCashflowBase> cashflows)`
- `void calculateAndStoreEir(Long batchExecutionId)`

### `LocfAmortizationService`

역할:

- 계약별 상각 상세 계산

메서드:

- `List<LocfAmortizationDetail> calculateAmortization(...)`
- `void calculateAndStoreAmortization(Long batchExecutionId)`

### `LocfResultAggregationService`

역할:

- 상세를 헤더/요약으로 집계

메서드:

- `void aggregateResultHeader(Long batchExecutionId)`
- `void aggregateResultSummary(Long batchExecutionId)`

### `LocfQueryService`

역할:

- 화면 응답 조립

메서드:

- `LocfContractResultResponse getContractResult(String contractNo)`
- `List<LocfSummaryItemResponse> getSummary(LocalDate baseDate)`

## 8. MyBatis Mapper 설계

### `LocfBatchControlMapper`

메서드:

- `int insertBatchExecution(LocfBatchExecution execution)`
- `int updateBatchExecutionStarted(LocfBatchExecution execution)`
- `int completeBatchExecution(LocfBatchExecution execution)`
- `int failBatchExecution(LocfBatchExecution execution)`
- `int insertStepExecution(LocfBatchStepExecution stepExecution)`
- `int completeStepExecution(LocfBatchStepExecution stepExecution)`
- `int failStepExecution(LocfBatchStepExecution stepExecution)`
- `List<LocfBatchExecution> findBatchHistoryByBaseDate(@Param("baseDate") LocalDate baseDate)`

### `LocfTargetContractMapper`

메서드:

- `int deleteTargetContractsByBatchExecutionId(@Param("batchExecutionId") Long batchExecutionId)`
- `int insertTargetContracts(@Param("batchExecutionId") Long batchExecutionId, @Param("baseDate") LocalDate baseDate)`
- `List<LocfTargetContract> findTargetContractsByBatchExecutionId(@Param("batchExecutionId") Long batchExecutionId)`
- `long countTargetContracts(@Param("batchExecutionId") Long batchExecutionId)`

실무 포인트:

- `insertTargetContracts`는 건건 insert가 아니라 `INSERT INTO ... SELECT`로 구현

### `LocfCashflowMapper`

메서드:

- `int deleteCashflowBaseByBatchExecutionId(@Param("batchExecutionId") Long batchExecutionId)`
- `int insertCashflowBaseRow(LocfCashflowBase row)`
- `List<LocfCashflowBase> findCashflowBaseByBatchExecutionId(@Param("batchExecutionId") Long batchExecutionId)`
- `List<LocfCashflowBase> findCashflowBaseByContractId(@Param("batchExecutionId") Long batchExecutionId, @Param("contractId") Long contractId)`

초기 구현:

- Java에서 계산 후 `insertCashflowBaseRow` 반복 사용 가능

확장 구현:

- Oracle 프로시저 호출용 `callGenerateCashflowBase(...)` 추가 가능

### `LocfEirMapper`

메서드:

- `int deleteEirResultsByBatchExecutionId(@Param("batchExecutionId") Long batchExecutionId)`
- `int insertEirResult(LocfEirResult result)`
- `LocfEirResult findEirResultByContractId(@Param("batchExecutionId") Long batchExecutionId, @Param("contractId") Long contractId)`
- `List<LocfEirResult> findEirResultsByBatchExecutionId(@Param("batchExecutionId") Long batchExecutionId)`

### `LocfAmortizationMapper`

메서드:

- `int deleteAmortizationDetailsByBatchExecutionId(@Param("batchExecutionId") Long batchExecutionId)`
- `int insertAmortizationDetail(LocfAmortizationDetail detail)`
- `int insertResultHeaders(@Param("batchExecutionId") Long batchExecutionId)`
- `int insertResultSummary(@Param("batchExecutionId") Long batchExecutionId)`
- `List<LocfAmortizationDetail> findAmortizationDetailsByContractId(@Param("batchExecutionId") Long batchExecutionId, @Param("contractId") Long contractId)`

실무 포인트:

- `insertResultHeaders`, `insertResultSummary`는 `INSERT INTO ... SELECT` 집계 SQL로 구현

### `LocfQueryMapper`

메서드:

- `LocfResultHeader findLatestResultHeaderByContractNo(@Param("contractNo") String contractNo)`
- `List<LocfAmortizationDetail> findLatestResultDetailsByContractNo(@Param("contractNo") String contractNo)`
- `List<LocfResultSummary> findSummaryByBaseDate(@Param("baseDate") LocalDate baseDate)`

## 9. MyBatis XML 역할 분리

권장 XML 파일:

- `LocfBatchControlMapper.xml`
- `LocfTargetContractMapper.xml`
- `LocfCashflowMapper.xml`
- `LocfEirMapper.xml`
- `LocfAmortizationMapper.xml`
- `LocfQueryMapper.xml`

### XML별 역할

`LocfBatchControlMapper.xml`

- 배치 시작/종료
- 실행 이력 조회

`LocfTargetContractMapper.xml`

- 대상 계약 대량 추출
- 기준일 필터링

`LocfCashflowMapper.xml`

- 현금흐름 base insert
- 계약별 현금흐름 조회

`LocfEirMapper.xml`

- EIR 저장/조회

`LocfAmortizationMapper.xml`

- 상각 상세 저장
- 헤더 요약 적재
- 요약 집계 적재

`LocfQueryMapper.xml`

- WebSquare 화면용 조인 조회

## 10. WebSquare 화면 설계

### 화면 1. 배치 실행/이력 화면

데이터셋:

- `dmBatchRun`
- `dmBatchSearch`
- `dlBatchHistory`

버튼:

- `btnRunBatch`
- `btnSearchBatchHistory`

Submission:

- `sbmRunBatch`
- `sbmSearchBatchHistory`

### 화면 2. 계약별 결과 조회 화면

데이터셋:

- `dmContractSearch`
- `dmResultHeader`
- `dlResultDetail`

버튼:

- `btnSearchContract`

Submission:

- `sbmSearchContractResult`

### 화면 3. 기준일 요약 화면

데이터셋:

- `dmSummarySearch`
- `dlSummary`

버튼:

- `btnSearchSummary`

Submission:

- `sbmSearchSummary`

## 11. API 응답 구조 예시

### 계약별 결과 조회 응답

```json
{
  "success": true,
  "data": {
    "contractNo": "LN-2026-000001",
    "customerName": "홍길동",
    "productCode": "MORTGAGE",
    "baseDate": "2026-05-10",
    "annualEir": 0.15180599,
    "initialCarryingAmount": 9870000.00,
    "finalCarryingAmount": 0.00,
    "details": [
      {
        "installmentNo": 1,
        "paymentDate": "2026-06-10",
        "scheduledPaymentAmt": 890828.63,
        "scheduledInterestAmt": 104166.67,
        "effectiveInterestRevenue": 105813.55,
        "netAmortizationAmt": 1646.88,
        "closingCarryingAmount": 9184981.37
      }
    ]
  },
  "error": null,
  "timestamp": "2026-05-10T12:00:00"
}
```

## 12. 개발 순서

### 1단계

- `LOCF_*` 테이블 DDL 작성
- 시퀀스/인덱스 생성

### 2단계

- `feature.locf.entity`
- `feature.locf.dto`
- `feature.locf.mapper`
- `mapper/locf/*.xml`
생성

### 3단계

- `LocfBatchControlMapper`
- `LocfTargetContractMapper`
- `LocfQueryMapper`
먼저 구현

### 4단계

- `LocfEirService`
- `LocfAmortizationService`
구현

### 5단계

- `LocfBatchServiceImpl`
- `LocfQueryServiceImpl`
- `LocfBatchController`
- `LocfQueryController`
구현

### 6단계

- WebSquare XML
- HTML 샘플
- 통합 테스트

## 13. 현재 프로젝트에 적용할 때의 현실적 변경 순서

현재 프로젝트는 LOCF 중심 구조로 전환하는 것을 기준으로 합니다.

### 1. LOCF 패키지 생성

- `feature.locf` 기준으로 신규 패키지 생성

### 2. LOCF DDL 생성

- `LOCF_*` 테이블과 시퀀스 생성

### 3. LOCF 화면 분리

- `locf-batch-sample.html`
- `locf-contract-result.xml`

### 4. 기능 검증

- 배치 실행
- 계약별 상세 조회
- 기준일자 요약 조회

## 14. 결론

실무형 LOCF 구현은 단순히 `계약 1건 계산기`를 늘리는 방식이 아니라, `원천 -> 대상 -> 현금흐름 -> EIR -> 상각 -> 결과 -> 조회`를 명확히 분리한 배치 엔진으로 설계해야 합니다.

현재 프로젝트에서는 다음이 가장 현실적인 구현 순서입니다.

- Oracle에 `LOCF_*` 배치 구조 추가
- Spring Boot에 `feature.locf` 패키지 추가
- MyBatis Mapper/XML로 대량 추출과 조회 구현
- EIR/상각 계산은 초기에는 Java 서비스로 구현
- 이후 성능 요구에 따라 Oracle 프로시저 또는 집합 SQL로 이관
