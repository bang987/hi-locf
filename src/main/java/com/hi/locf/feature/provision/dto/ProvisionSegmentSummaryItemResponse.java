package com.hi.locf.feature.provision.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProvisionSegmentSummaryItemResponse(
        LocalDate baseDate,
        String customerType,
        String segmentCode,
        String stageCode,
        long contractCount,
        BigDecimal totalCarryingAmount,
        BigDecimal totalEadAmount,
        BigDecimal totalEclAmount
) {
}
