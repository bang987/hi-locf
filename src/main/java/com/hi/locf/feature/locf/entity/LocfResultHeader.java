package com.hi.locf.feature.locf.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LocfResultHeader {

    private Long resultHdrId;
    private Long batchExecutionId;
    private String batchRunNo;
    private LocalDate baseDate;
    private Long contractId;
    private String contractNo;
    private Long customerId;
    private String customerName;
    private String productCode;
    private BigDecimal principalAmt;
    private BigDecimal initialCarryingAmount;
    private BigDecimal annualEir;
    private BigDecimal finalCarryingAmount;
    private BigDecimal totalEffectiveInterestRev;
    private BigDecimal totalFeeAmortizationAmt;
    private BigDecimal totalCostAmortizationAmt;

    public Long getResultHdrId() {
        return resultHdrId;
    }

    public void setResultHdrId(Long resultHdrId) {
        this.resultHdrId = resultHdrId;
    }

    public Long getBatchExecutionId() {
        return batchExecutionId;
    }

    public void setBatchExecutionId(Long batchExecutionId) {
        this.batchExecutionId = batchExecutionId;
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

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public BigDecimal getPrincipalAmt() {
        return principalAmt;
    }

    public void setPrincipalAmt(BigDecimal principalAmt) {
        this.principalAmt = principalAmt;
    }

    public BigDecimal getInitialCarryingAmount() {
        return initialCarryingAmount;
    }

    public void setInitialCarryingAmount(BigDecimal initialCarryingAmount) {
        this.initialCarryingAmount = initialCarryingAmount;
    }

    public BigDecimal getAnnualEir() {
        return annualEir;
    }

    public void setAnnualEir(BigDecimal annualEir) {
        this.annualEir = annualEir;
    }

    public BigDecimal getFinalCarryingAmount() {
        return finalCarryingAmount;
    }

    public void setFinalCarryingAmount(BigDecimal finalCarryingAmount) {
        this.finalCarryingAmount = finalCarryingAmount;
    }

    public BigDecimal getTotalEffectiveInterestRev() {
        return totalEffectiveInterestRev;
    }

    public void setTotalEffectiveInterestRev(BigDecimal totalEffectiveInterestRev) {
        this.totalEffectiveInterestRev = totalEffectiveInterestRev;
    }

    public BigDecimal getTotalFeeAmortizationAmt() {
        return totalFeeAmortizationAmt;
    }

    public void setTotalFeeAmortizationAmt(BigDecimal totalFeeAmortizationAmt) {
        this.totalFeeAmortizationAmt = totalFeeAmortizationAmt;
    }

    public BigDecimal getTotalCostAmortizationAmt() {
        return totalCostAmortizationAmt;
    }

    public void setTotalCostAmortizationAmt(BigDecimal totalCostAmortizationAmt) {
        this.totalCostAmortizationAmt = totalCostAmortizationAmt;
    }
}
