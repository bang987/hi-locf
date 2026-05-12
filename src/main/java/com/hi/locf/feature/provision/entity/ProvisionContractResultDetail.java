package com.hi.locf.feature.provision.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProvisionContractResultDetail {

    private Long resultDtlId;
    private Long batchExecutionId;
    private LocalDate baseDate;
    private Long contractId;
    private String contractNo;
    private String customerName;
    private String productCode;
    private String stageCode;
    private String stageReason;
    private BigDecimal carryingAmount;
    private BigDecimal eadAmount;
    private BigDecimal oneYearPdRate;
    private BigDecimal lifetimePdRate;
    private BigDecimal lgdRate;
    private BigDecimal recoveryRate;
    private BigDecimal eclAmount;

    public Long getResultDtlId() { return resultDtlId; }
    public void setResultDtlId(Long resultDtlId) { this.resultDtlId = resultDtlId; }
    public Long getBatchExecutionId() { return batchExecutionId; }
    public void setBatchExecutionId(Long batchExecutionId) { this.batchExecutionId = batchExecutionId; }
    public LocalDate getBaseDate() { return baseDate; }
    public void setBaseDate(LocalDate baseDate) { this.baseDate = baseDate; }
    public Long getContractId() { return contractId; }
    public void setContractId(Long contractId) { this.contractId = contractId; }
    public String getContractNo() { return contractNo; }
    public void setContractNo(String contractNo) { this.contractNo = contractNo; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }
    public String getStageCode() { return stageCode; }
    public void setStageCode(String stageCode) { this.stageCode = stageCode; }
    public String getStageReason() { return stageReason; }
    public void setStageReason(String stageReason) { this.stageReason = stageReason; }
    public BigDecimal getCarryingAmount() { return carryingAmount; }
    public void setCarryingAmount(BigDecimal carryingAmount) { this.carryingAmount = carryingAmount; }
    public BigDecimal getEadAmount() { return eadAmount; }
    public void setEadAmount(BigDecimal eadAmount) { this.eadAmount = eadAmount; }
    public BigDecimal getOneYearPdRate() { return oneYearPdRate; }
    public void setOneYearPdRate(BigDecimal oneYearPdRate) { this.oneYearPdRate = oneYearPdRate; }
    public BigDecimal getLifetimePdRate() { return lifetimePdRate; }
    public void setLifetimePdRate(BigDecimal lifetimePdRate) { this.lifetimePdRate = lifetimePdRate; }
    public BigDecimal getLgdRate() { return lgdRate; }
    public void setLgdRate(BigDecimal lgdRate) { this.lgdRate = lgdRate; }
    public BigDecimal getRecoveryRate() { return recoveryRate; }
    public void setRecoveryRate(BigDecimal recoveryRate) { this.recoveryRate = recoveryRate; }
    public BigDecimal getEclAmount() { return eclAmount; }
    public void setEclAmount(BigDecimal eclAmount) { this.eclAmount = eclAmount; }
}
