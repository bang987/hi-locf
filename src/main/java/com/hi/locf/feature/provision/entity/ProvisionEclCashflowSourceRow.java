package com.hi.locf.feature.provision.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProvisionEclCashflowSourceRow {

    private Long batchExecutionId;
    private Long targetId;
    private Long contractId;
    private String contractNo;
    private LocalDate baseDate;
    private String stageCode;
    private BigDecimal oneYearPdRate;
    private BigDecimal lifetimePdRate;
    private BigDecimal lgdRate;
    private BigDecimal monthlyEir;
    private Long installmentNo;
    private LocalDate cashflowDate;
    private BigDecimal beginningEadAmount;
    private BigDecimal expectedPrincipalAmt;
    private BigDecimal expectedInterestAmt;
    private BigDecimal endingEadAmount;

    public Long getBatchExecutionId() { return batchExecutionId; }
    public void setBatchExecutionId(Long batchExecutionId) { this.batchExecutionId = batchExecutionId; }
    public Long getTargetId() { return targetId; }
    public void setTargetId(Long targetId) { this.targetId = targetId; }
    public Long getContractId() { return contractId; }
    public void setContractId(Long contractId) { this.contractId = contractId; }
    public String getContractNo() { return contractNo; }
    public void setContractNo(String contractNo) { this.contractNo = contractNo; }
    public LocalDate getBaseDate() { return baseDate; }
    public void setBaseDate(LocalDate baseDate) { this.baseDate = baseDate; }
    public String getStageCode() { return stageCode; }
    public void setStageCode(String stageCode) { this.stageCode = stageCode; }
    public BigDecimal getOneYearPdRate() { return oneYearPdRate; }
    public void setOneYearPdRate(BigDecimal oneYearPdRate) { this.oneYearPdRate = oneYearPdRate; }
    public BigDecimal getLifetimePdRate() { return lifetimePdRate; }
    public void setLifetimePdRate(BigDecimal lifetimePdRate) { this.lifetimePdRate = lifetimePdRate; }
    public BigDecimal getLgdRate() { return lgdRate; }
    public void setLgdRate(BigDecimal lgdRate) { this.lgdRate = lgdRate; }
    public BigDecimal getMonthlyEir() { return monthlyEir; }
    public void setMonthlyEir(BigDecimal monthlyEir) { this.monthlyEir = monthlyEir; }
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
}
