package com.hi.locf.feature.provision.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProvisionSummaryItemResponse {

    private LocalDate baseDate;
    private String productCode;
    private String stageCode;
    private long contractCount;
    private BigDecimal totalCarryingAmount;
    private BigDecimal totalEadAmount;
    private BigDecimal totalEclAmount;

    public ProvisionSummaryItemResponse() {
    }

    public ProvisionSummaryItemResponse(LocalDate baseDate, String productCode, String stageCode, long contractCount,
            BigDecimal totalCarryingAmount, BigDecimal totalEadAmount, BigDecimal totalEclAmount) {
        this.baseDate = baseDate;
        this.productCode = productCode;
        this.stageCode = stageCode;
        this.contractCount = contractCount;
        this.totalCarryingAmount = totalCarryingAmount;
        this.totalEadAmount = totalEadAmount;
        this.totalEclAmount = totalEclAmount;
    }

    public LocalDate getBaseDate() {
        return baseDate;
    }

    public void setBaseDate(LocalDate baseDate) {
        this.baseDate = baseDate;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getStageCode() {
        return stageCode;
    }

    public void setStageCode(String stageCode) {
        this.stageCode = stageCode;
    }

    public long getContractCount() {
        return contractCount;
    }

    public void setContractCount(long contractCount) {
        this.contractCount = contractCount;
    }

    public BigDecimal getTotalCarryingAmount() {
        return totalCarryingAmount;
    }

    public void setTotalCarryingAmount(BigDecimal totalCarryingAmount) {
        this.totalCarryingAmount = totalCarryingAmount;
    }

    public BigDecimal getTotalEadAmount() {
        return totalEadAmount;
    }

    public void setTotalEadAmount(BigDecimal totalEadAmount) {
        this.totalEadAmount = totalEadAmount;
    }

    public BigDecimal getTotalEclAmount() {
        return totalEclAmount;
    }

    public void setTotalEclAmount(BigDecimal totalEclAmount) {
        this.totalEclAmount = totalEclAmount;
    }
}
