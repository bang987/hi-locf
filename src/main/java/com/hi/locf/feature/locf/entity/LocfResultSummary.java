package com.hi.locf.feature.locf.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LocfResultSummary {

    private Long summaryId;
    private Long batchExecutionId;
    private LocalDate baseDate;
    private String productCode;
    private Long contractCount;
    private BigDecimal totalInitialCarryingAmt;
    private BigDecimal totalFinalCarryingAmt;
    private BigDecimal totalEffectiveInterestRev;

    public Long getSummaryId() {
        return summaryId;
    }

    public void setSummaryId(Long summaryId) {
        this.summaryId = summaryId;
    }

    public Long getBatchExecutionId() {
        return batchExecutionId;
    }

    public void setBatchExecutionId(Long batchExecutionId) {
        this.batchExecutionId = batchExecutionId;
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

    public Long getContractCount() {
        return contractCount;
    }

    public void setContractCount(Long contractCount) {
        this.contractCount = contractCount;
    }

    public BigDecimal getTotalInitialCarryingAmt() {
        return totalInitialCarryingAmt;
    }

    public void setTotalInitialCarryingAmt(BigDecimal totalInitialCarryingAmt) {
        this.totalInitialCarryingAmt = totalInitialCarryingAmt;
    }

    public BigDecimal getTotalFinalCarryingAmt() {
        return totalFinalCarryingAmt;
    }

    public void setTotalFinalCarryingAmt(BigDecimal totalFinalCarryingAmt) {
        this.totalFinalCarryingAmt = totalFinalCarryingAmt;
    }

    public BigDecimal getTotalEffectiveInterestRev() {
        return totalEffectiveInterestRev;
    }

    public void setTotalEffectiveInterestRev(BigDecimal totalEffectiveInterestRev) {
        this.totalEffectiveInterestRev = totalEffectiveInterestRev;
    }
}
