package com.hi.locf.feature.locf.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public class LocfBatchRunRequest {

    @NotNull(message = "기준일자는 필수입니다.")
    private LocalDate baseDate;

    private String batchType = "MANUAL";

    public LocalDate getBaseDate() {
        return baseDate;
    }

    public void setBaseDate(LocalDate baseDate) {
        this.baseDate = baseDate;
    }

    public String getBatchType() {
        return batchType;
    }

    public void setBatchType(String batchType) {
        this.batchType = batchType;
    }
}
