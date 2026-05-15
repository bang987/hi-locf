package com.hi.locf.feature.locf.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LocfSourceContractItemResponse {

    private String contractNo;
    private String customerName;
    private String productCode;
    private String repaymentType;
    private LocalDate executionDate;
    private LocalDate maturityDate;
    private BigDecimal principalAmt;
    private BigDecimal annualNominalRate;
    private BigDecimal outstandingPrincipalAmt;

    public LocfSourceContractItemResponse() {
    }

    public LocfSourceContractItemResponse(String contractNo, String customerName, String productCode,
            String repaymentType, LocalDate executionDate, LocalDate maturityDate, BigDecimal principalAmt,
            BigDecimal annualNominalRate, BigDecimal outstandingPrincipalAmt) {
        this.contractNo = contractNo;
        this.customerName = customerName;
        this.productCode = productCode;
        this.repaymentType = repaymentType;
        this.executionDate = executionDate;
        this.maturityDate = maturityDate;
        this.principalAmt = principalAmt;
        this.annualNominalRate = annualNominalRate;
        this.outstandingPrincipalAmt = outstandingPrincipalAmt;
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

    public String getRepaymentType() {
        return repaymentType;
    }

    public void setRepaymentType(String repaymentType) {
        this.repaymentType = repaymentType;
    }

    public LocalDate getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(LocalDate executionDate) {
        this.executionDate = executionDate;
    }

    public LocalDate getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(LocalDate maturityDate) {
        this.maturityDate = maturityDate;
    }

    public BigDecimal getPrincipalAmt() {
        return principalAmt;
    }

    public void setPrincipalAmt(BigDecimal principalAmt) {
        this.principalAmt = principalAmt;
    }

    public BigDecimal getAnnualNominalRate() {
        return annualNominalRate;
    }

    public void setAnnualNominalRate(BigDecimal annualNominalRate) {
        this.annualNominalRate = annualNominalRate;
    }

    public BigDecimal getOutstandingPrincipalAmt() {
        return outstandingPrincipalAmt;
    }

    public void setOutstandingPrincipalAmt(BigDecimal outstandingPrincipalAmt) {
        this.outstandingPrincipalAmt = outstandingPrincipalAmt;
    }
}
