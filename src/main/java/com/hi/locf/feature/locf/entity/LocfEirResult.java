package com.hi.locf.feature.locf.entity;

import java.math.BigDecimal;

public class LocfEirResult {

    private Long eirResultId;
    private Long batchExecutionId;
    private Long contractId;
    private String contractNo;
    private BigDecimal initialCarryingAmount;
    private BigDecimal monthlyEir;
    private BigDecimal annualEir;

    public static LocfEirResult create(
            Long batchExecutionId,
            Long contractId,
            String contractNo,
            BigDecimal initialCarryingAmount,
            BigDecimal monthlyEir,
            BigDecimal annualEir
    ) {
        LocfEirResult result = new LocfEirResult();
        result.batchExecutionId = batchExecutionId;
        result.contractId = contractId;
        result.contractNo = contractNo;
        result.initialCarryingAmount = initialCarryingAmount;
        result.monthlyEir = monthlyEir;
        result.annualEir = annualEir;
        return result;
    }

    public Long getEirResultId() { return eirResultId; }
    public void setEirResultId(Long eirResultId) { this.eirResultId = eirResultId; }
    public Long getBatchExecutionId() { return batchExecutionId; }
    public void setBatchExecutionId(Long batchExecutionId) { this.batchExecutionId = batchExecutionId; }
    public Long getContractId() { return contractId; }
    public void setContractId(Long contractId) { this.contractId = contractId; }
    public String getContractNo() { return contractNo; }
    public void setContractNo(String contractNo) { this.contractNo = contractNo; }
    public BigDecimal getInitialCarryingAmount() { return initialCarryingAmount; }
    public void setInitialCarryingAmount(BigDecimal initialCarryingAmount) { this.initialCarryingAmount = initialCarryingAmount; }
    public BigDecimal getMonthlyEir() { return monthlyEir; }
    public void setMonthlyEir(BigDecimal monthlyEir) { this.monthlyEir = monthlyEir; }
    public BigDecimal getAnnualEir() { return annualEir; }
    public void setAnnualEir(BigDecimal annualEir) { this.annualEir = annualEir; }
}
