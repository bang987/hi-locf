package com.hi.locf.feature.locf.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LocfBatchHistoryItemResponse {

    private String batchRunNo;
    private LocalDate baseDate;
    private String batchType;
    private String statusCode;
    private long targetCount;
    private long processedCount;
    private long errorCount;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private String errorMessage;

    public LocfBatchHistoryItemResponse() {
    }

    public LocfBatchHistoryItemResponse(String batchRunNo, LocalDate baseDate, String batchType, String statusCode,
            long targetCount, long processedCount, long errorCount, LocalDateTime startedAt,
            LocalDateTime finishedAt, String errorMessage) {
        this.batchRunNo = batchRunNo;
        this.baseDate = baseDate;
        this.batchType = batchType;
        this.statusCode = statusCode;
        this.targetCount = targetCount;
        this.processedCount = processedCount;
        this.errorCount = errorCount;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
        this.errorMessage = errorMessage;
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

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
