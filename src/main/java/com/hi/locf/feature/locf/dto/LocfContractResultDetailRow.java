package com.hi.locf.feature.locf.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LocfContractResultDetailRow(
        long installmentNo,
        LocalDate paymentDate,
        BigDecimal openingPrincipalBal,
        BigDecimal openingCarryingAmt,
        BigDecimal scheduledPaymentAmt,
        BigDecimal scheduledPrincipalAmt,
        BigDecimal scheduledInterestAmt,
        BigDecimal effectiveInterestRevenue,
        BigDecimal feeAmortizationAmt,
        BigDecimal costAmortizationAmt,
        BigDecimal netAmortizationAmt,
        BigDecimal closingPrincipalBal,
        BigDecimal closingCarryingAmt
) {
}
