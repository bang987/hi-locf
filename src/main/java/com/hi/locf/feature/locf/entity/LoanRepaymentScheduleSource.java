package com.hi.locf.feature.locf.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class LoanRepaymentScheduleSource {

    private Long scheduleId;
    private Long contractId;
    private Long installmentNo;
    private LocalDate paymentDate;
    private BigDecimal scheduledPaymentAmt;
    private BigDecimal scheduledPrincipalAmt;
    private BigDecimal scheduledInterestAmt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static LoanRepaymentScheduleSource create(
            Long contractId,
            Long installmentNo,
            LocalDate paymentDate,
            BigDecimal scheduledPaymentAmt,
            BigDecimal scheduledPrincipalAmt,
            BigDecimal scheduledInterestAmt
    ) {
        // 회차별 원천 상환스케줄 1행을 생성한다.
        LocalDateTime now = LocalDateTime.now();
        LoanRepaymentScheduleSource row = new LoanRepaymentScheduleSource();
        row.contractId = contractId;
        row.installmentNo = installmentNo;
        row.paymentDate = paymentDate;
        row.scheduledPaymentAmt = scheduledPaymentAmt;
        row.scheduledPrincipalAmt = scheduledPrincipalAmt;
        row.scheduledInterestAmt = scheduledInterestAmt;
        row.createdAt = now;
        row.updatedAt = now;
        return row;
    }

    public Long getScheduleId() { return scheduleId; }
    public void setScheduleId(Long scheduleId) { this.scheduleId = scheduleId; }
    public Long getContractId() { return contractId; }
    public void setContractId(Long contractId) { this.contractId = contractId; }
    public Long getInstallmentNo() { return installmentNo; }
    public void setInstallmentNo(Long installmentNo) { this.installmentNo = installmentNo; }
    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }
    public BigDecimal getScheduledPaymentAmt() { return scheduledPaymentAmt; }
    public void setScheduledPaymentAmt(BigDecimal scheduledPaymentAmt) { this.scheduledPaymentAmt = scheduledPaymentAmt; }
    public BigDecimal getScheduledPrincipalAmt() { return scheduledPrincipalAmt; }
    public void setScheduledPrincipalAmt(BigDecimal scheduledPrincipalAmt) { this.scheduledPrincipalAmt = scheduledPrincipalAmt; }
    public BigDecimal getScheduledInterestAmt() { return scheduledInterestAmt; }
    public void setScheduledInterestAmt(BigDecimal scheduledInterestAmt) { this.scheduledInterestAmt = scheduledInterestAmt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
