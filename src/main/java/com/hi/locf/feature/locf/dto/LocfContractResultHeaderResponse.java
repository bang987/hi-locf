package com.hi.locf.feature.locf.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LocfContractResultHeaderResponse {

    private String batchRunNo;
    private LocalDate baseDate;
    private String contractNo;
    private String customerName;
    private String productCode;
    private BigDecimal principalAmt;
    private BigDecimal initialCarryingAmount;
    private BigDecimal annualEir;
    private BigDecimal finalCarryingAmount;
    private BigDecimal totalEffectiveInterestRev;
    private BigDecimal totalFeeAmortizationAmt;
    private BigDecimal totalCostAmortizationAmt;

    public LocfContractResultHeaderResponse() {
    }

    public LocfContractResultHeaderResponse(String batchRunNo, LocalDate baseDate, String contractNo,
            String customerName, String productCode, BigDecimal principalAmt, BigDecimal initialCarryingAmount,
            BigDecimal annualEir, BigDecimal finalCarryingAmount, BigDecimal totalEffectiveInterestRev,
            BigDecimal totalFeeAmortizationAmt, BigDecimal totalCostAmortizationAmt) {
        this.batchRunNo = batchRunNo;
        this.baseDate = baseDate;
        this.contractNo = contractNo;
        this.customerName = customerName;
        this.productCode = productCode;
        this.principalAmt = principalAmt;
        this.initialCarryingAmount = initialCarryingAmount;
        this.annualEir = annualEir;
        this.finalCarryingAmount = finalCarryingAmount;
        this.totalEffectiveInterestRev = totalEffectiveInterestRev;
        this.totalFeeAmortizationAmt = totalFeeAmortizationAmt;
        this.totalCostAmortizationAmt = totalCostAmortizationAmt;
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

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
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
