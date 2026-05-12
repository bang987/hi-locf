package com.hi.locf.feature.provision.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProvisionResultSummary {

    private Long summaryId;
    private Long batchExecutionId;
    private LocalDate baseDate;
    private String productCode;
    private String stageCode;
    private Long contractCount;
    private BigDecimal totalCarryingAmount;
    private BigDecimal totalEadAmount;
    private BigDecimal totalEclAmount;

    public Long getSummaryId() { return summaryId; }
    public void setSummaryId(Long summaryId) { this.summaryId = summaryId; }
    public Long getBatchExecutionId() { return batchExecutionId; }
    public void setBatchExecutionId(Long batchExecutionId) { this.batchExecutionId = batchExecutionId; }
    public LocalDate getBaseDate() { return baseDate; }
    public void setBaseDate(LocalDate baseDate) { this.baseDate = baseDate; }
    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }
    public String getStageCode() { return stageCode; }
    public void setStageCode(String stageCode) { this.stageCode = stageCode; }
    public Long getContractCount() { return contractCount; }
    public void setContractCount(Long contractCount) { this.contractCount = contractCount; }
    public BigDecimal getTotalCarryingAmount() { return totalCarryingAmount; }
    public void setTotalCarryingAmount(BigDecimal totalCarryingAmount) { this.totalCarryingAmount = totalCarryingAmount; }
    public BigDecimal getTotalEadAmount() { return totalEadAmount; }
    public void setTotalEadAmount(BigDecimal totalEadAmount) { this.totalEadAmount = totalEadAmount; }
    public BigDecimal getTotalEclAmount() { return totalEclAmount; }
    public void setTotalEclAmount(BigDecimal totalEclAmount) { this.totalEclAmount = totalEclAmount; }
}
