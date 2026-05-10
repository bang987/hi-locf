package com.hi.locf.feature.locf.entity;

import java.time.LocalDateTime;

public class LocfBatchStepExecution {

    private Long stepExecutionId;
    private Long batchExecutionId;
    private String stepName;
    private String statusCode;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private Long processedCount;
    private String errorMessage;

    public static LocfBatchStepExecution start(Long batchExecutionId, String stepName) {
        LocfBatchStepExecution step = new LocfBatchStepExecution();
        step.batchExecutionId = batchExecutionId;
        step.stepName = stepName;
        step.statusCode = "RUNNING";
        step.startedAt = LocalDateTime.now();
        step.processedCount = 0L;
        return step;
    }

    public void complete(long processedCount) {
        this.statusCode = "COMPLETED";
        this.processedCount = processedCount;
        this.finishedAt = LocalDateTime.now();
    }

    public void fail(long processedCount, String errorMessage) {
        this.statusCode = "FAILED";
        this.processedCount = processedCount;
        this.errorMessage = errorMessage;
        this.finishedAt = LocalDateTime.now();
    }

    public Long getStepExecutionId() {
        return stepExecutionId;
    }

    public void setStepExecutionId(Long stepExecutionId) {
        this.stepExecutionId = stepExecutionId;
    }

    public Long getBatchExecutionId() {
        return batchExecutionId;
    }

    public void setBatchExecutionId(Long batchExecutionId) {
        this.batchExecutionId = batchExecutionId;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
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

    public Long getProcessedCount() {
        return processedCount;
    }

    public void setProcessedCount(Long processedCount) {
        this.processedCount = processedCount;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
