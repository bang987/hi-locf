package com.hi.locf.feature.locf.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class LoanBalanceSource {

    private Long balanceId;
    private Long contractId;
    private LocalDate baseDate;
    private BigDecimal outstandingPrincipalAmt;
    private BigDecimal accruedInterestAmt;
    private String statusCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static LoanBalanceSource create(Long contractId, LocalDate baseDate, BigDecimal outstandingPrincipalAmt, BigDecimal accruedInterestAmt) {
        // 기준일 현재 원천 잔액 1건을 생성한다.
        LocalDateTime now = LocalDateTime.now();
        LoanBalanceSource balance = new LoanBalanceSource();
        balance.contractId = contractId;
        balance.baseDate = baseDate;
        balance.outstandingPrincipalAmt = outstandingPrincipalAmt;
        balance.accruedInterestAmt = accruedInterestAmt;
        balance.statusCode = "ACTIVE";
        balance.createdAt = now;
        balance.updatedAt = now;
        return balance;
    }

    public Long getBalanceId() { return balanceId; }
    public void setBalanceId(Long balanceId) { this.balanceId = balanceId; }
    public Long getContractId() { return contractId; }
    public void setContractId(Long contractId) { this.contractId = contractId; }
    public LocalDate getBaseDate() { return baseDate; }
    public void setBaseDate(LocalDate baseDate) { this.baseDate = baseDate; }
    public BigDecimal getOutstandingPrincipalAmt() { return outstandingPrincipalAmt; }
    public void setOutstandingPrincipalAmt(BigDecimal outstandingPrincipalAmt) { this.outstandingPrincipalAmt = outstandingPrincipalAmt; }
    public BigDecimal getAccruedInterestAmt() { return accruedInterestAmt; }
    public void setAccruedInterestAmt(BigDecimal accruedInterestAmt) { this.accruedInterestAmt = accruedInterestAmt; }
    public String getStatusCode() { return statusCode; }
    public void setStatusCode(String statusCode) { this.statusCode = statusCode; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
