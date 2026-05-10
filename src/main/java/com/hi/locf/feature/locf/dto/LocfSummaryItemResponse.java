package com.hi.locf.feature.locf.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LocfSummaryItemResponse(
        LocalDate baseDate,
        String productCode,
        long contractCount,
        BigDecimal totalInitialCarryingAmt,
        BigDecimal totalFinalCarryingAmt,
        BigDecimal totalEffectiveInterestRev
) {
}
