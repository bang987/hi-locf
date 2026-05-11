package com.hi.locf.feature.locf.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LocfBatchExecution {

    private Long batchExecutionId;
    private String batchRunNo;
    private LocalDate baseDate;
    private String batchType;
    private String statusCode;
    private Long targetCount;
    private Long processedCount;
    private Long errorCount;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private String errorMessage;

    public static LocfBatchExecution start(String batchRunNo, LocalDate baseDate, String batchType) {
        // 배치 헤더 1건을 RUNNING 상태로 시작한다.
        LocfBatchExecution execution = new LocfBatchExecution();
        execution.batchRunNo = batchRunNo;
        execution.baseDate = baseDate;
        execution.batchType = batchType;
        execution.statusCode = "RUNNING";
        execution.targetCount = 0L;
        execution.processedCount = 0L;
        execution.errorCount = 0L;
        execution.startedAt = LocalDateTime.now();
        return execution;
    }

    public void complete(long targetCount, long processedCount, long errorCount) {
        // 모든 step이 정상 종료되면 완료 상태와 최종 건수를 기록한다.
        this.statusCode = "COMPLETED";
        this.targetCount = targetCount;
        this.processedCount = processedCount;
        this.errorCount = errorCount;
        this.finishedAt = LocalDateTime.now();
    }

    public void fail(String errorMessage) {
        // 예외 발생 시 실패 상태와 오류 메시지를 남긴다.
        this.statusCode = "FAILED";
        this.errorCount = 1L;
        this.errorMessage = errorMessage;
        this.finishedAt = LocalDateTime.now();
    }

    public Long getBatchExecutionId() {
        return batchExecutionId;
    }

    public void setBatchExecutionId(Long batchExecutionId) {
        this.batchExecutionId = batchExecutionId;
    }

    public String getBatchRunNo() {
        return batchRunNo;
    }

    public void setBatchRunNo(String batchRunNo) {
        this.batchRunNo = batchRunNo;
    }

    public LocalDate getBaseDate() {
        return baseDate;
    }

    public void setBaseDate(LocalDate baseDate) {
        this.baseDate = baseDate;
    }

    public String getBatchType() {
        return batchType;
    }

    public void setBatchType(String batchType) {
        this.batchType = batchType;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Long getTargetCount() {
        return targetCount;
    }

    public void setTargetCount(Long targetCount) {
        this.targetCount = targetCount;
    }

    public Long getProcessedCount() {
        return processedCount;
    }

    public void setProcessedCount(Long processedCount) {
        this.processedCount = processedCount;
    }

    public Long getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(Long errorCount) {
        this.errorCount = errorCount;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(LocalDateTime finishedAt) {
        this.finishedAt = finishedAt;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
