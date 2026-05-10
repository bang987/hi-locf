package com.hi.locf.feature.locf.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LocfContractResultHeaderResponse(
        String batchRunNo,
        LocalDate baseDate,
        String contractNo,
        String customerName,
        String productCode,
        BigDecimal principalAmt,
        BigDecimal initialCarryingAmount,
        BigDecimal annualEir,
        BigDecimal finalCarryingAmount,
        BigDecimal totalEffectiveInterestRev,
        BigDecimal totalFeeAmortizationAmt,
        BigDecimal totalCostAmortizationAmt
) {
}
