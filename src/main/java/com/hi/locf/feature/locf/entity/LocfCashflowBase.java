package com.hi.locf.feature.locf.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LocfCashflowBase {

    private Long cashflowBaseId;
    private Long batchExecutionId;
    private Long contractId;
    private String contractNo;
    private Long installmentNo;
    private LocalDate paymentDate;
    private BigDecimal scheduledPaymentAmt;
    private BigDecimal scheduledPrincipalAmt;
    private BigDecimal scheduledInterestAmt;

    public static LocfCashflowBase create(Long batchExecutionId, Long contractId, String contractNo, LoanRepaymentScheduleSource row) {
        // 원천 상환스케줄 1행을 LOCF 계산용 약정 현금흐름 1행으로 변환한다.
        LocfCashflowBase base = new LocfCashflowBase();
        base.batchExecutionId = batchExecutionId;
        base.contractId = contractId;
        base.contractNo = contractNo;
        base.installmentNo = row.getInstallmentNo();
        base.paymentDate = row.getPaymentDate();
        base.scheduledPaymentAmt = row.getScheduledPaymentAmt();
        base.scheduledPrincipalAmt = row.getScheduledPrincipalAmt();
        base.scheduledInterestAmt = row.getScheduledInterestAmt();
        return base;
    }

    public Long getCashflowBaseId() { return cashflowBaseId; }
    public void setCashflowBaseId(Long cashflowBaseId) { this.cashflowBaseId = cashflowBaseId; }
    public Long getBatchExecutionId() { return batchExecutionId; }
    public void setBatchExecutionId(Long batchExecutionId) { this.batchExecutionId = batchExecutionId; }
    public Long getContractId() { return contractId; }
    public void setContractId(Long contractId) { this.contractId = contractId; }
    public String getContractNo() { return contractNo; }
    public void setContractNo(String contractNo) { this.contractNo = contractNo; }
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
}
