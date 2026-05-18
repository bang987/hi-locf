package com.hi.locf.feature.closing.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MonthlyClosingStatusResponse {

    private LocalDate baseDate;
    private String locfBatchRunNo;
    private String locfStatusCode;
    private LocalDateTime locfFinishedAt;
    private String provisionBatchRunNo;
    private String provisionStatusCode;
    private LocalDateTime provisionFinishedAt;
    private String closingStatusCode;
    private String nextAction;

    public LocalDate getBaseDate() { return baseDate; }
    public void setBaseDate(LocalDate baseDate) { this.baseDate = baseDate; }
    public String getLocfBatchRunNo() { return locfBatchRunNo; }
    public void setLocfBatchRunNo(String locfBatchRunNo) { this.locfBatchRunNo = locfBatchRunNo; }
    public String getLocfStatusCode() { return locfStatusCode; }
    public void setLocfStatusCode(String locfStatusCode) { this.locfStatusCode = locfStatusCode; }
    public LocalDateTime getLocfFinishedAt() { return locfFinishedAt; }
    public void setLocfFinishedAt(LocalDateTime locfFinishedAt) { this.locfFinishedAt = locfFinishedAt; }
    public String getProvisionBatchRunNo() { return provisionBatchRunNo; }
    public void setProvisionBatchRunNo(String provisionBatchRunNo) { this.provisionBatchRunNo = provisionBatchRunNo; }
    public String getProvisionStatusCode() { return provisionStatusCode; }
    public void setProvisionStatusCode(String provisionStatusCode) { this.provisionStatusCode = provisionStatusCode; }
    public LocalDateTime getProvisionFinishedAt() { return provisionFinishedAt; }
    public void setProvisionFinishedAt(LocalDateTime provisionFinishedAt) { this.provisionFinishedAt = provisionFinishedAt; }
    public String getClosingStatusCode() { return closingStatusCode; }
    public void setClosingStatusCode(String closingStatusCode) { this.closingStatusCode = closingStatusCode; }
    public String getNextAction() { return nextAction; }
    public void setNextAction(String nextAction) { this.nextAction = nextAction; }
}
