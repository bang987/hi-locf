package com.hi.locf.feature.locf.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class LoanRateSource {

    private Long rateId;
    private Long contractId;
    private String rateType;
    private BigDecimal annualNominalRate;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static LoanRateSource create(Long contractId, BigDecimal annualNominalRate, LocalDate effectiveFrom, LocalDate effectiveTo) {
        LoanRateSource rate = new LoanRateSource();
        rate.contractId = contractId;
        rate.rateType = "FIXED";
        rate.annualNominalRate = annualNominalRate;
        rate.effectiveFrom = effectiveFrom;
        rate.effectiveTo = effectiveTo;
        rate.createdAt = LocalDateTime.now();
        rate.updatedAt = LocalDateTime.now();
        return rate;
    }

    public Long getRateId() { return rateId; }
    public void setRateId(Long rateId) { this.rateId = rateId; }
    public Long getContractId() { return contractId; }
    public void setContractId(Long contractId) { this.contractId = contractId; }
    public String getRateType() { return rateType; }
    public void setRateType(String rateType) { this.rateType = rateType; }
    public BigDecimal getAnnualNominalRate() { return annualNominalRate; }
    public void setAnnualNominalRate(BigDecimal annualNominalRate) { this.annualNominalRate = annualNominalRate; }
    public LocalDate getEffectiveFrom() { return effectiveFrom; }
    public void setEffectiveFrom(LocalDate effectiveFrom) { this.effectiveFrom = effectiveFrom; }
    public LocalDate getEffectiveTo() { return effectiveTo; }
    public void setEffectiveTo(LocalDate effectiveTo) { this.effectiveTo = effectiveTo; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
