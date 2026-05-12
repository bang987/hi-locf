package com.hi.locf.feature.locf.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LoanDelinquencySource {

    private Long delinquencyId;
    private Long contractId;
    private LocalDate baseDate;
    private Long delinquencyDays;
    private String defaultYn;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static LoanDelinquencySource create(Long contractId, LocalDate baseDate, Long delinquencyDays, String defaultYn) {
        LocalDateTime now = LocalDateTime.now();
        LoanDelinquencySource delinquency = new LoanDelinquencySource();
        delinquency.contractId = contractId;
        delinquency.baseDate = baseDate;
        delinquency.delinquencyDays = delinquencyDays;
        delinquency.defaultYn = defaultYn;
        delinquency.createdAt = now;
        delinquency.updatedAt = now;
        return delinquency;
    }

    public Long getDelinquencyId() { return delinquencyId; }
    public void setDelinquencyId(Long delinquencyId) { this.delinquencyId = delinquencyId; }
    public Long getContractId() { return contractId; }
    public void setContractId(Long contractId) { this.contractId = contractId; }
    public LocalDate getBaseDate() { return baseDate; }
    public void setBaseDate(LocalDate baseDate) { this.baseDate = baseDate; }
    public Long getDelinquencyDays() { return delinquencyDays; }
    public void setDelinquencyDays(Long delinquencyDays) { this.delinquencyDays = delinquencyDays; }
    public String getDefaultYn() { return defaultYn; }
    public void setDefaultYn(String defaultYn) { this.defaultYn = defaultYn; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
