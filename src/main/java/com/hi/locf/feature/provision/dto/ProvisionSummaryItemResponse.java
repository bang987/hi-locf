package com.hi.locf.feature.provision.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProvisionSummaryItemResponse(
        LocalDate baseDate,
        String productCode,
        String stageCode,
        long contractCount,
        BigDecimal totalCarryingAmount,
        BigDecimal totalEadAmount,
        BigDecimal totalEclAmount
) {
}
