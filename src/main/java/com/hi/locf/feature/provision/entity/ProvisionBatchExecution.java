package com.hi.locf.feature.provision.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ProvisionBatchExecution {

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

    public static ProvisionBatchExecution start(String batchRunNo, LocalDate baseDate, String batchType) {
        ProvisionBatchExecution execution = new ProvisionBatchExecution();
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
        this.statusCode = "COMPLETED";
        this.targetCount = targetCount;
        this.processedCount = processedCount;
        this.errorCount = errorCount;
        this.finishedAt = LocalDateTime.now();
    }

    public void fail(String errorMessage) {
        this.statusCode = "FAILED";
        this.errorCount = 1L;
        this.errorMessage = errorMessage;
        this.finishedAt = LocalDateTime.now();
    }

    public Long getBatchExecutionId() { return batchExecutionId; }
    public void setBatchExecutionId(Long batchExecutionId) { this.batchExecutionId = batchExecutionId; }
    public String getBatchRunNo() { return batchRunNo; }
    public void setBatchRunNo(String batchRunNo) { this.batchRunNo = batchRunNo; }
    public LocalDate getBaseDate() { return baseDate; }
    public void setBaseDate(LocalDate baseDate) { this.baseDate = baseDate; }
    public String getBatchType() { return batchType; }
    public void setBatchType(String batchType) { this.batchType = batchType; }
    public String getStatusCode() { return statusCode; }
    public void setStatusCode(String statusCode) { this.statusCode = statusCode; }
    public Long getTargetCount() { return targetCount; }
    public void setTargetCount(Long targetCount) { this.targetCount = targetCount; }
    public Long getProcessedCount() { return processedCount; }
    public void setProcessedCount(Long processedCount) { this.processedCount = processedCount; }
    public Long getErrorCount() { return errorCount; }
    public void setErrorCount(Long errorCount) { this.errorCount = errorCount; }
    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }
    public LocalDateTime getFinishedAt() { return finishedAt; }
    public void setFinishedAt(LocalDateTime finishedAt) { this.finishedAt = finishedAt; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}
