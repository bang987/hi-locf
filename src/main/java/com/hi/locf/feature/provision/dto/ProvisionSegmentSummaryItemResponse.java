package com.hi.locf.feature.provision.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProvisionSegmentSummaryItemResponse {

    private LocalDate baseDate;
    private String customerType;
    private String segmentCode;
    private String stageCode;
    private Long contractCount;
    private BigDecimal totalCarryingAmount;
    private BigDecimal totalEadAmount;
    private BigDecimal totalEclAmount;

    public LocalDate getBaseDate() {
        return baseDate;
    }

    public void setBaseDate(LocalDate baseDate) {
        this.baseDate = baseDate;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getSegmentCode() {
        return segmentCode;
    }

    public void setSegmentCode(String segmentCode) {
        this.segmentCode = segmentCode;
    }

    public String getStageCode() {
        return stageCode;
    }

    public void setStageCode(String stageCode) {
        this.stageCode = stageCode;
    }

    public Long getContractCount() {
        return contractCount;
    }

    public void setContractCount(Long contractCount) {
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
