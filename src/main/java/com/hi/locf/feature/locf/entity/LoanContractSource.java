package com.hi.locf.feature.locf.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class LoanContractSource {

    private Long contractId;
    private String contractNo;
    private Long customerId;
    private String productCode;
    private String loanTypeCode;
    private String repaymentType;
    private String customerName;
    private LocalDate executionDate;
    private LocalDate maturityDate;
    private BigDecimal principalAmt;
    private BigDecimal feeAmt;
    private BigDecimal directCostAmt;
    private BigDecimal annualNominalRate;
    private BigDecimal outstandingPrincipalAmt;
    private String statusCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static LoanContractSource create(
            String contractNo,
            Long customerId,
            String productCode,
            String loanTypeCode,
            String repaymentType,
            LocalDate executionDate,
            LocalDate maturityDate,
            BigDecimal principalAmt,
            BigDecimal feeAmt,
            BigDecimal directCostAmt
    ) {
        // 원천 여신계약 1건을 생성한다. 샘플 데이터 기준 기본 상태는 ACTIVE로 둔다.
        LocalDateTime now = LocalDateTime.now();
        LoanContractSource contract = new LoanContractSource();
        contract.contractNo = contractNo;
        contract.customerId = customerId;
        contract.productCode = productCode;
        contract.loanTypeCode = loanTypeCode;
        contract.repaymentType = repaymentType;
        contract.executionDate = executionDate;
        contract.maturityDate = maturityDate;
        contract.principalAmt = principalAmt;
        contract.feeAmt = feeAmt;
        contract.directCostAmt = directCostAmt;
        contract.statusCode = "ACTIVE";
        contract.createdAt = now;
        contract.updatedAt = now;
        return contract;
    }

    public Long getContractId() { return contractId; }
    public void setContractId(Long contractId) { this.contractId = contractId; }
    public String getContractNo() { return contractNo; }
    public void setContractNo(String contractNo) { this.contractNo = contractNo; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }
    public String getLoanTypeCode() { return loanTypeCode; }
    public void setLoanTypeCode(String loanTypeCode) { this.loanTypeCode = loanTypeCode; }
    public String getRepaymentType() { return repaymentType; }
    public void setRepaymentType(String repaymentType) { this.repaymentType = repaymentType; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public LocalDate getExecutionDate() { return executionDate; }
    public void setExecutionDate(LocalDate executionDate) { this.executionDate = executionDate; }
    public LocalDate getMaturityDate() { return maturityDate; }
    public void setMaturityDate(LocalDate maturityDate) { this.maturityDate = maturityDate; }
    public BigDecimal getPrincipalAmt() { return principalAmt; }
    public void setPrincipalAmt(BigDecimal principalAmt) { this.principalAmt = principalAmt; }
    public BigDecimal getFeeAmt() { return feeAmt; }
    public void setFeeAmt(BigDecimal feeAmt) { this.feeAmt = feeAmt; }
    public BigDecimal getDirectCostAmt() { return directCostAmt; }
    public void setDirectCostAmt(BigDecimal directCostAmt) { this.directCostAmt = directCostAmt; }
    public BigDecimal getAnnualNominalRate() { return annualNominalRate; }
    public void setAnnualNominalRate(BigDecimal annualNominalRate) { this.annualNominalRate = annualNominalRate; }
    public BigDecimal getOutstandingPrincipalAmt() { return outstandingPrincipalAmt; }
    public void setOutstandingPrincipalAmt(BigDecimal outstandingPrincipalAmt) { this.outstandingPrincipalAmt = outstandingPrincipalAmt; }
    public String getStatusCode() { return statusCode; }
    public void setStatusCode(String statusCode) { this.statusCode = statusCode; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
