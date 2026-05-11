# LOCF WebSquare 연계 해설

## 목적
이 문서는 [locf-sample.xml](D:\sts-5.1.1.RELEASE\workspace\hi-locf\src\main\resources\static\websquare\locf-sample.xml)을 기준으로, WebSquare 화면이 Spring Boot LOCF API와 어떻게 연결되는지 학습용으로 설명한다.

핵심 흐름은 이렇다.

```text
WebSquare 입력/버튼
-> script 함수
-> submission
-> Spring Controller
-> Service
-> MyBatis Mapper
-> Oracle
-> JSON(ApiResponse)
-> script에서 response.data 추출
-> DataMap/DataList 적재
-> Grid/Input 자동 반영
```

## 파일 위치
- WebSquare XML: [locf-sample.xml](D:\sts-5.1.1.RELEASE\workspace\hi-locf\src\main\resources\static\websquare\locf-sample.xml)
- HTML 학습 화면: [locf-sample.html](D:\sts-5.1.1.RELEASE\workspace\hi-locf\src\main\resources\static\locf-sample.html)
- 배치 API: [LocfBatchController.java](D:\sts-5.1.1.RELEASE\workspace\hi-locf\src\main\java\com\hi\locf\feature\locf\controller\LocfBatchController.java)
- 조회 API: [LocfQueryController.java](D:\sts-5.1.1.RELEASE\workspace\hi-locf\src\main\java\com\hi\locf\feature\locf\controller\LocfQueryController.java)

## 1. DataMap / DataList
WebSquare는 먼저 화면에서 사용할 데이터 그릇을 정의한다.

### DataMap
단건 데이터 또는 입력 조건용이다.

- `dmSearch`
  - 기준일자
  - 요약 기준일자
  - 계약번호
  - 배치실행번호
- `dmBatchRunRequest`
  - 배치 실행 요청 본문
  - `baseDate`
  - `batchType`
- `dmBatchRunResult`
  - 배치 실행 결과 1건
- `dmContractHeader`
  - 계약별 결과 헤더 1건

### DataList
다건 목록 또는 Grid 바인딩용이다.

- `dlSourceContracts`
  - 원천 계약 목록
- `dlBatchHistory`
  - 배치 이력 목록
- `dlStepHistory`
  - 배치 step 이력 목록
- `dlContractDetails`
  - 계약별 회차 상세 목록
- `dlSummary`
  - 기준일자별 상품 요약 목록

즉 WebSquare 화면은 "컴포넌트가 직접 값을 가지는 구조"보다 "DataMap/DataList를 보고 그리는 구조"라고 이해하면 된다.

## 2. Submission
Submission은 "어떤 URL을 어떤 방식으로 호출할지"를 선언한다.

### 원천 계약 조회
```xml
<xf:submission id="sbmSourceContracts"
               action="http://localhost:8080/hi-locf/api/v1/locf/source-contracts"
               method="get"
               mediatype="application/json"/>
```

이 submission은 아래 API를 호출한다.
- [LocfQueryController.java](D:\sts-5.1.1.RELEASE\workspace\hi-locf\src\main\java\com\hi\locf\feature\locf\controller\LocfQueryController.java)
- `GET /hi-locf/api/v1/locf/source-contracts`

### 배치 실행
```xml
<xf:submission id="sbmRunBatch"
               ref="data:dmBatchRunRequest"
               action="http://localhost:8080/hi-locf/api/v1/locf/batches/run"
               method="post"
               mediatype="application/json"/>
```

이 submission은 아래 API를 호출한다.
- [LocfBatchController.java](D:\sts-5.1.1.RELEASE\workspace\hi-locf\src\main\java\com\hi\locf\feature\locf\controller\LocfBatchController.java)
- `POST /hi-locf/api/v1/locf/batches/run`

요청 본문은 `dmBatchRunRequest`에서 가져온다.

### 배치 이력 조회
```xml
<xf:submission id="sbmBatchHistory"
               action="http://localhost:8080/hi-locf/api/v1/locf/batches"
               method="get"
               mediatype="application/json"/>
```

호출 API:
- `GET /hi-locf/api/v1/locf/batches`

### Step 이력 조회
```xml
<xf:submission id="sbmStepHistory"
               method="get"
               mediatype="application/json"/>
```

이 submission은 URL이 동적이다. script에서 아래처럼 바꿔서 호출한다.

```javascript
$p.getSubmission("sbmStepHistory").setAction(
    "http://localhost:8080/hi-locf/api/v1/locf/batches/" + batchRunNo + "/steps"
);
```

호출 API:
- `GET /hi-locf/api/v1/locf/batches/{batchRunNo}/steps`

### 계약별 결과 조회
```javascript
$p.getSubmission("sbmContractResult").setAction(
    "http://localhost:8080/hi-locf/api/v1/locf/contracts/" + contractNo
);
```

호출 API:
- `GET /hi-locf/api/v1/locf/contracts/{contractNo}`

### 요약 조회
```javascript
$p.getSubmission("sbmSummary").setAction(
    "http://localhost:8080/hi-locf/api/v1/locf/results/summary?baseDate=" + baseDate
);
```

호출 API:
- `GET /hi-locf/api/v1/locf/results/summary?baseDate=...`

## 3. Script
Script는 화면 이벤트와 submission 호출을 연결한다.

### onload
```javascript
function scwin.onload() {
    scwin.loadSourceContracts();
    scwin.loadBatchHistory();
}
```

의미:
- 화면이 뜨자마자 원천 계약과 배치 이력을 먼저 조회한다.

### 배치 실행
```javascript
scwin.runBatch = function() {
    var baseDate = $p.getComponentById("iptBaseDate").getValue();
    var batchRequest = $p.getDataCollection("dmBatchRunRequest");

    batchRequest.set("baseDate", baseDate);
    batchRequest.set("batchType", "MANUAL");

    $p.getSubmission("sbmRunBatch").send();
};
```

의미:
1. 화면 입력값을 읽는다.
2. `dmBatchRunRequest`에 넣는다.
3. `sbmRunBatch`를 보낸다.

즉 WebSquare에서는 보통 "입력 컴포넌트 값 -> DataMap 반영 -> submission send" 순서로 생각하면 된다.

## 4. ApiResponse Wrapper 처리
현재 백엔드는 공통 응답 포맷을 사용한다.

예:
```json
{
  "success": true,
  "data": [...],
  "error": null,
  "timestamp": "2026-05-11T..."
}
```

그래서 WebSquare script에서는 응답 본문을 그대로 DataList에 넣지 않고, `response.data`를 꺼내야 한다.

예:
```javascript
scwin.handleListResponse = function(submissionId, responseText, dataListId) {
    var response = JSON.parse(responseText);
    if (!response.success) {
        alert(response.error ? response.error.message : "조회 중 오류가 발생했습니다.");
        return;
    }
    $p.getDataCollection(dataListId).setJSON(response.data);
};
```

이 부분이 학습상 매우 중요하다.

- Spring 응답 전체 JSON
- WebSquare DataSet에 넣을 실제 목록

이 둘은 다르다.

즉:
- `response` 전체는 API 공통 포맷
- `response.data`가 실제 Grid 바인딩 데이터

## 5. Grid 바인딩
Grid는 DataList 또는 DataMap을 바라본다.

예:
```xml
<w2:gridView id="grdSourceContracts" dataList="dlSourceContracts">
```

의미:
- `dlSourceContracts`에 값이 들어오면
- `grdSourceContracts`가 그 데이터를 표시한다.

예를 들어 `sbmSourceContracts_submitdone()`에서:

```javascript
scwin.handleListResponse("sbmSourceContracts", e.responseBody, "dlSourceContracts");
```

가 실행되면
- `dlSourceContracts`에 목록이 들어가고
- `grdSourceContracts`가 자동 갱신된다.

## 6. API - Controller - Service - Mapper 연결표

### 원천 계약 조회
- WebSquare submission: `sbmSourceContracts`
- API: `GET /hi-locf/api/v1/locf/source-contracts`
- Controller: [LocfQueryController.java](D:\sts-5.1.1.RELEASE\workspace\hi-locf\src\main\java\com\hi\locf\feature\locf\controller\LocfQueryController.java) `getSourceContracts()`
- Service: [LocfQueryServiceImpl.java](D:\sts-5.1.1.RELEASE\workspace\hi-locf\src\main\java\com\hi\locf\feature\locf\service\LocfQueryServiceImpl.java) `getSourceContracts()`
- Mapper: `LocfQueryMapper.findSourceContracts()`
- XML: [LocfQueryMapper.xml](D:\sts-5.1.1.RELEASE\workspace\hi-locf\src\main\resources\mapper\locf\LocfQueryMapper.xml)

### 배치 실행
- WebSquare submission: `sbmRunBatch`
- API: `POST /hi-locf/api/v1/locf/batches/run`
- Controller: [LocfBatchController.java](D:\sts-5.1.1.RELEASE\workspace\hi-locf\src\main\java\com\hi\locf\feature\locf\controller\LocfBatchController.java) `runBatch()`
- Service: [LocfBatchServiceImpl.java](D:\sts-5.1.1.RELEASE\workspace\hi-locf\src\main\java\com\hi\locf\feature\locf\service\LocfBatchServiceImpl.java) `runBatch()`
- Mapper:
  - `LocfBatchControlMapper`
  - `LocfTargetContractMapper`
  - `LocfSourceDataMapper`
  - `LocfCashflowMapper`
  - `LocfEirMapper`
  - `LocfAmortizationMapper`

### 배치 step 이력 조회
- WebSquare submission: `sbmStepHistory`
- API: `GET /hi-locf/api/v1/locf/batches/{batchRunNo}/steps`
- Controller: `LocfBatchController.getStepHistory()`
- Service: `LocfBatchServiceImpl.getStepHistory()`
- Mapper: `LocfBatchControlMapper.findStepHistoryByBatchRunNo()`

### 계약별 결과 조회
- WebSquare submission: `sbmContractResult`
- API: `GET /hi-locf/api/v1/locf/contracts/{contractNo}`
- Controller: `LocfQueryController.getContractResult()`
- Service: `LocfQueryServiceImpl.getContractResult()`
- Mapper:
  - `LocfQueryMapper.findLatestResultHeaderByContractNo()`
  - `LocfQueryMapper.findLatestResultDetailsByContractNo()`

### 요약 조회
- WebSquare submission: `sbmSummary`
- API: `GET /hi-locf/api/v1/locf/results/summary?baseDate=...`
- Controller: `LocfQueryController.getSummary()`
- Service: `LocfQueryServiceImpl.getSummary()`
- Mapper: `LocfQueryMapper.findSummaryByBaseDate()`

## 7. 화면 학습 순서
학습은 보통 아래 순서가 가장 좋다.

1. `dmSearch`, `dmBatchRunRequest`, `dlSourceContracts` 같은 데이터 그릇을 먼저 본다.
2. `sbmSourceContracts`, `sbmRunBatch` 같은 submission URL을 본다.
3. script 함수에서 어떤 submission을 호출하는지 본다.
4. 같은 이름의 Spring API를 Controller에서 찾는다.
5. Service와 Mapper로 내려가 데이터가 어떻게 조회/저장되는지 본다.
6. 다시 submitdone 에서 `response.data`가 어떤 DataSet에 들어가는지 본다.
7. 그 DataSet을 바라보는 Grid가 무엇인지 확인한다.

## 8. HTML 버전과 WebSquare 버전 차이

### HTML
- JavaScript `fetch`
- DOM 직접 렌더링
- `renderTable()` 함수로 테이블 생성

### WebSquare
- DataMap/DataList 중심
- submission 호출
- submitdone 에서 dataset 반영
- gridView가 dataset을 자동 표시

즉 같은 백엔드를 쓰더라도 화면 철학이 다르다.

- HTML: JS가 직접 그린다
- WebSquare: DataSet이 바뀌면 화면이 따라간다

## 9. 주의할 점
- 현재 XML은 학습용 예시다.
- WebSquare 실제 프로젝트에서는 사내 공통 submission, 공통 메시지 처리, 공통 헤더 처리 로직이 들어갈 수 있다.
- `ApiResponse` wrapper 때문에 `response.data` 추출이 필요하다.
- 동적 URL은 script에서 `setAction()`으로 바꾼다.
- 실제 런타임이 있어야 브라우저에서 WebSquare 컴포넌트로 렌더링된다.

## 10. 한 줄 정리
[locf-sample.xml](D:\sts-5.1.1.RELEASE\workspace\hi-locf\src\main\resources\static\websquare\locf-sample.xml)은
"원천조회 -> 배치실행 -> step이력 -> 계약상세 -> 요약조회" LOCF 학습 흐름을
WebSquare 방식의 DataSet / Submission / Script / Grid 구조로 옮겨 놓은 예제다.
