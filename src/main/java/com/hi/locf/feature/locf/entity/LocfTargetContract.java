package com.hi.locf.feature.locf.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LocfTargetContract {

    private Long targetId;
    private Long batchExecutionId;
    private Long contractId;
    private String contractNo;
    private Long customerId;
    private String customerName;
    private String productCode;
    private String repaymentType;
    private BigDecimal principalAmt;
    private BigDecimal feeAmt;
    private BigDecimal directCostAmt;
    private BigDecimal annualNominalRate;
    private LocalDate executionDate;
    private LocalDate maturityDate;
    private Long termInMonths;

    public Long getTargetId() { return targetId; }
    public void setTargetId(Long targetId) { this.targetId = targetId; }
    public Long getBatchExecutionId() { return batchExecutionId; }
    public void setBatchExecutionId(Long batchExecutionId) { this.batchExecutionId = batchExecutionId; }
    public Long getContractId() { return contractId; }
    public void setContractId(Long contractId) { this.contractId = contractId; }
    public String getContractNo() { return contractNo; }
    public void setContractNo(String contractNo) { this.contractNo = contractNo; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }
    public String getRepaymentType() { return repaymentType; }
    public void setRepaymentType(String repaymentType) { this.repaymentType = repaymentType; }
    public BigDecimal getPrincipalAmt() { return principalAmt; }
    public void setPrincipalAmt(BigDecimal principalAmt) { this.principalAmt = principalAmt; }
    public BigDecimal getFeeAmt() { return feeAmt; }
    public void setFeeAmt(BigDecimal feeAmt) { this.feeAmt = feeAmt; }
    public BigDecimal getDirectCostAmt() { return directCostAmt; }
    public void setDirectCostAmt(BigDecimal directCostAmt) { this.directCostAmt = directCostAmt; }
    public BigDecimal getAnnualNominalRate() { return annualNominalRate; }
    public void setAnnualNominalRate(BigDecimal annualNominalRate) { this.annualNominalRate = annualNominalRate; }
    public LocalDate getExecutionDate() { return executionDate; }
    public void setExecutionDate(LocalDate executionDate) { this.executionDate = executionDate; }
    public LocalDate getMaturityDate() { return maturityDate; }
    public void setMaturityDate(LocalDate maturityDate) { this.maturityDate = maturityDate; }
    public Long getTermInMonths() { return termInMonths; }
    public void setTermInMonths(Long termInMonths) { this.termInMonths = termInMonths; }
}
