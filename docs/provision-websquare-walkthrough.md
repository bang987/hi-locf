# 충당금 WebSquare 연계 해설

## 목적
이 문서는 [provision-sample.xml](D:\sts-5.1.1.RELEASE\workspace\hi-locf\src\main\resources\static\websquare\provision-sample.xml)을 기준으로, WebSquare 화면이 Spring Boot 충당금 API와 어떻게 연결되는지 학습용으로 설명한다.

핵심 흐름은 이렇다.

```text
WebSquare 입력/버튼
-> script 함수
-> submission
-> Spring Controller
-> Service
-> MyBatis Mapper
-> Oracle 프로시저 / SQL
-> JSON(ApiResponse)
-> script에서 response.data 추출
-> DataList/DataMap 적재
-> Grid 자동 반영
```

## 파일 위치
- WebSquare XML: [provision-sample.xml](D:\sts-5.1.1.RELEASE\workspace\hi-locf\src\main\resources\static\websquare\provision-sample.xml)
- HTML 학습 화면: [provision-sample.html](D:\sts-5.1.1.RELEASE\workspace\hi-locf\src\main\resources\static\provision-sample.html)
- 배치 API: [ProvisionBatchController.java](D:\sts-5.1.1.RELEASE\workspace\hi-locf\src\main\java\com\hi\locf\feature\provision\controller\ProvisionBatchController.java)
- 조회 API: [ProvisionQueryController.java](D:\sts-5.1.1.RELEASE\workspace\hi-locf\src\main\java\com\hi\locf\feature\provision\controller\ProvisionQueryController.java)

## 1. DataMap / DataList

### DataMap
- `dmBatchRunRequest`
  - 배치 실행 요청 본문
  - `baseDate`
  - `batchType`
- `dmBatchRunResult`
  - 배치 실행 결과 1건

### DataList
- `dlBatchHistory`
  - 배치 이력
- `dlStepHistory`
  - 배치 step 이력
- `dlContractResults`
  - 계약별 Stage / EAD / PD / LGD / ECL 결과
- `dlSummary`
  - 기준일자별 상품 / Stage 요약

즉 충당금 화면은 "배치 실행 이력 + 결과 목록" 중심이다.

## 2. Submission

### 배치 실행
```xml
<xf:submission id="sbmRunBatch"
               ref="data:dmBatchRunRequest"
               action="http://localhost:8080/hi-locf/api/v1/provision/batches/run"
               method="post"
               mediatype="application/json"/>
```

호출 API:
- `POST /hi-locf/api/v1/provision/batches/run`

### 배치 이력 조회
`sbmBatchHistory`는 script에서 기준일자 쿼리스트링을 붙인다.

호출 API:
- `GET /hi-locf/api/v1/provision/batches?baseDate=...`

### Step 이력 조회
`sbmStepHistory`는 배치실행번호를 URL 경로에 넣어 호출한다.

호출 API:
- `GET /hi-locf/api/v1/provision/batches/{batchRunNo}/steps`

### 계약별 충당금 결과 조회
`sbmContractResult`는 계약번호를 URL 경로에 넣어 호출한다.

호출 API:
- `GET /hi-locf/api/v1/provision/contracts/{contractNo}`

### 요약 조회
`sbmSummary`는 기준일자를 쿼리스트링으로 붙여 호출한다.

호출 API:
- `GET /hi-locf/api/v1/provision/results/summary?baseDate=...`

## 3. Script
Script는 WebSquare 입력 이벤트와 submission 호출을 연결한다.

### 배치 실행
```javascript
scwin.runBatch = function() {
    var baseDate = $p.getComponentById("iptBaseDate").getValue();
    var request = $p.getDataCollection("dmBatchRunRequest");
    request.set("baseDate", baseDate);
    request.set("batchType", "MANUAL");
    $p.getSubmission("sbmRunBatch").send();
};
```

의미:
1. 입력 컴포넌트에서 기준일자를 읽는다.
2. `dmBatchRunRequest`에 넣는다.
3. submission을 보낸다.

### 공통 응답 처리
백엔드는 `ApiResponse { success, data, error, timestamp }` 형식으로 응답한다.

그래서 script에서는 `response.data`를 꺼내 DataList에 넣는다.

```javascript
scwin.handleListResponse = function(responseText, dataListId) {
    var response = JSON.parse(responseText);
    if (!response.success) {
        alert(response.error ? response.error.message : "조회 중 오류가 발생했습니다.");
        return;
    }
    $p.getDataCollection(dataListId).setJSON(response.data);
};
```

## 4. Controller - Service - Mapper 연결표

### 배치 실행
- WebSquare submission: `sbmRunBatch`
- API: `POST /hi-locf/api/v1/provision/batches/run`
- Controller: `ProvisionBatchController.runBatch()`
- Service: `ProvisionBatchServiceImpl.runBatch()`
- Mapper:
  - `ProvisionBatchControlMapper`
  - `ProvisionCalculationMapper`

### 배치 이력 조회
- API: `GET /hi-locf/api/v1/provision/batches`
- Controller: `ProvisionBatchController.getBatchHistory()`
- Service: `ProvisionBatchServiceImpl.getBatchHistory()`
- Mapper: `ProvisionBatchControlMapper.findBatchHistoryByBaseDate()`

### Step 이력 조회
- API: `GET /hi-locf/api/v1/provision/batches/{batchRunNo}/steps`
- Controller: `ProvisionBatchController.getStepHistory()`
- Service: `ProvisionBatchServiceImpl.getStepHistory()`
- Mapper: `ProvisionBatchControlMapper.findStepHistoryByBatchRunNo()`

### 계약별 결과 조회
- API: `GET /hi-locf/api/v1/provision/contracts/{contractNo}`
- Controller: `ProvisionQueryController.getContractResult()`
- Service: `ProvisionQueryServiceImpl.getContractResult()`
- Mapper: `ProvisionQueryMapper.findLatestContractResultsByContractNo()`

### 요약 조회
- API: `GET /hi-locf/api/v1/provision/results/summary?baseDate=...`
- Controller: `ProvisionQueryController.getSummary()`
- Service: `ProvisionQueryServiceImpl.getSummary()`
- Mapper: `ProvisionQueryMapper.findSummaryByBaseDate()`

## 5. 실무 학습 포인트

### 배치 쪽
충당금 배치는 LOCF보다 DB 프로시저 비중이 더 크다.

- `PRC_PRV_CLEAR_BATCH_DATA`
- `PRC_PRV_INSERT_TARGET_CONTRACTS`
- `PRC_PRV_CALCULATE_STAGE`
- `PRC_PRV_CALCULATE_EAD`
- `PRC_PRV_CALCULATE_PD`
- `PRC_PRV_CALCULATE_LGD`
- `PRC_PRV_CALCULATE_ECL`
- `PRC_PRV_INSERT_RESULT_SUMMARY`

즉 WebSquare 화면에서 버튼 하나를 눌러도, 실제 내부에서는 Java Service가 여러 프로시저를 step 순서로 호출한다.

### 조회 쪽
계약별 조회는 단건이 아니라 `List<ProvisionContractResultRow>`로 내려온다. 실무에서는 계약 1건이라도 세그먼트/Stage/재실행 이력에 따라 여러 행이 올 수 있다고 생각하면 된다.

## 6. HTML 버전과 차이

### HTML
- `fetch`
- DOM 직접 렌더링

### WebSquare
- DataList 중심
- submission
- submitdone
- gridView 자동 반영

즉 HTML은 화면 스크립트가 직접 그리는 방식이고, WebSquare는 DataSet을 바꾸면 화면이 따라오는 방식이다.

## 7. 한 줄 정리
[provision-sample.xml](D:\sts-5.1.1.RELEASE\workspace\hi-locf\src\main\resources\static\websquare\provision-sample.xml)은
"충당금 배치 실행 -> Step 확인 -> 계약별 결과 조회 -> 요약 조회" 흐름을
WebSquare DataMap/DataList/Submission 방식으로 학습하기 위한 예제다.
