package com.hi.locf.feature.provision.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProvisionEclCashflowDetail {

    private Long eclCashflowId;
    private Long batchExecutionId;
    private Long targetId;
    private Long contractId;
    private String contractNo;
    private Long installmentNo;
    private LocalDate cashflowDate;
    private BigDecimal beginningEadAmount;
    private BigDecimal expectedPrincipalAmt;
    private BigDecimal expectedInterestAmt;
    private BigDecimal endingEadAmount;
    private BigDecimal marginalPdRate;
    private BigDecimal cumulativePdRate;
    private BigDecimal lgdRate;
    private BigDecimal discountRate;
    private BigDecimal discountFactor;
    private BigDecimal periodEclAmount;
    private BigDecimal pvEclAmount;

    public Long getEclCashflowId() { return eclCashflowId; }
    public void setEclCashflowId(Long eclCashflowId) { this.eclCashflowId = eclCashflowId; }
    public Long getBatchExecutionId() { return batchExecutionId; }
    public void setBatchExecutionId(Long batchExecutionId) { this.batchExecutionId = batchExecutionId; }
    public Long getTargetId() { return targetId; }
    public void setTargetId(Long targetId) { this.targetId = targetId; }
    public Long getContractId() { return contractId; }
    public void setContractId(Long contractId) { this.contractId = contractId; }
    public String getContractNo() { return contractNo; }
    public void setContractNo(String contractNo) { this.contractNo = contractNo; }
    public Long getInstallmentNo() { return installmentNo; }
    public void setInstallmentNo(Long installmentNo) { this.installmentNo = installmentNo; }
    public LocalDate getCashflowDate() { return cashflowDate; }
    public void setCashflowDate(LocalDate cashflowDate) { this.cashflowDate = cashflowDate; }
    public BigDecimal getBeginningEadAmount() { return beginningEadAmount; }
    public void setBeginningEadAmount(BigDecimal beginningEadAmount) { this.beginningEadAmount = beginningEadAmount; }
    public BigDecimal getExpectedPrincipalAmt() { return expectedPrincipalAmt; }
    public void setExpectedPrincipalAmt(BigDecimal expectedPrincipalAmt) { this.expectedPrincipalAmt = expectedPrincipalAmt; }
    public BigDecimal getExpectedInterestAmt() { return expectedInterestAmt; }
    public void setExpectedInterestAmt(BigDecimal expectedInterestAmt) { this.expectedInterestAmt = expectedInterestAmt; }
    public BigDecimal getEndingEadAmount() { return endingEadAmount; }
    public void setEndingEadAmount(BigDecimal endingEadAmount) { this.endingEadAmount = endingEadAmount; }
    public BigDecimal getMarginalPdRate() { return marginalPdRate; }
    public void setMarginalPdRate(BigDecimal marginalPdRate) { this.marginalPdRate = marginalPdRate; }
    public BigDecimal getCumulativePdRate() { return cumulativePdRate; }
    public void setCumulativePdRate(BigDecimal cumulativePdRate) { this.cumulativePdRate = cumulativePdRate; }
    public BigDecimal getLgdRate() { return lgdRate; }
    public void setLgdRate(BigDecimal lgdRate) { this.lgdRate = lgdRate; }
    public BigDecimal getDiscountRate() { return discountRate; }
    public void setDiscountRate(BigDecimal discountRate) { this.discountRate = discountRate; }
    public BigDecimal getDiscountFactor() { return discountFactor; }
    public void setDiscountFactor(BigDecimal discountFactor) { this.discountFactor = discountFactor; }
    public BigDecimal getPeriodEclAmount() { return periodEclAmount; }
    public void setPeriodEclAmount(BigDecimal periodEclAmount) { this.periodEclAmount = periodEclAmount; }
    public BigDecimal getPvEclAmount() { return pvEclAmount; }
    public void setPvEclAmount(BigDecimal pvEclAmount) { this.pvEclAmount = pvEclAmount; }
}
