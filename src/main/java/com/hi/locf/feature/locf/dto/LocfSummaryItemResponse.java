package com.hi.locf.feature.locf.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LocfSummaryItemResponse {

    private LocalDate baseDate;
    private String productCode;
    private long contractCount;
    private BigDecimal totalInitialCarryingAmt;
    private BigDecimal totalFinalCarryingAmt;
    private BigDecimal totalEffectiveInterestRev;

    public LocfSummaryItemResponse() {
    }

    public LocfSummaryItemResponse(LocalDate baseDate, String productCode, long contractCount,
            BigDecimal totalInitialCarryingAmt, BigDecimal totalFinalCarryingAmt,
            BigDecimal totalEffectiveInterestRev) {
        this.baseDate = baseDate;
        this.productCode = productCode;
        this.contractCount = contractCount;
        this.totalInitialCarryingAmt = totalInitialCarryingAmt;
        this.totalFinalCarryingAmt = totalFinalCarryingAmt;
        this.totalEffectiveInterestRev = totalEffectiveInterestRev;
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

    public long getContractCount() {
        return contractCount;
    }

    public void setContractCount(long contractCount) {
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
