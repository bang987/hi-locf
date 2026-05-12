package com.hi.locf.feature.provision.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public class ProvisionBatchRunRequest {

    @NotNull(message = "기준일자는 필수입니다.")
    private LocalDate baseDate;

    private String batchType;

    public LocalDate getBaseDate() { return baseDate; }
    public void setBaseDate(LocalDate baseDate) { this.baseDate = baseDate; }
    public String getBatchType() { return batchType; }
    public void setBatchType(String batchType) { this.batchType = batchType; }
}
