package com.hi.locf.feature.provision.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ProvisionBatchRunResponse {

    private String batchRunNo;
    private LocalDate baseDate;
    private String statusCode;
    private long targetCount;
    private long processedCount;
    private long errorCount;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;

    public ProvisionBatchRunResponse() {
    }

    public ProvisionBatchRunResponse(String batchRunNo, LocalDate baseDate, String statusCode, long targetCount,
            long processedCount, long errorCount, LocalDateTime startedAt, LocalDateTime finishedAt) {
        this.batchRunNo = batchRunNo;
        this.baseDate = baseDate;
        this.statusCode = statusCode;
        this.targetCount = targetCount;
        this.processedCount = processedCount;
        this.errorCount = errorCount;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
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

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public long getTargetCount() {
        return targetCount;
    }

    public void setTargetCount(long targetCount) {
        this.targetCount = targetCount;
    }

    public long getProcessedCount() {
        return processedCount;
    }

    public void setProcessedCount(long processedCount) {
        this.processedCount = processedCount;
    }

    public long getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(long errorCount) {
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
}
