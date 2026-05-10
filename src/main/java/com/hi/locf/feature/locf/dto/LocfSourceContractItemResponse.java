package com.hi.locf.feature.locf.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LocfSourceContractItemResponse(
        String contractNo,
        String customerName,
        String productCode,
        String repaymentType,
        LocalDate executionDate,
        LocalDate maturityDate,
        BigDecimal principalAmt,
        BigDecimal annualNominalRate,
        BigDecimal outstandingPrincipalAmt
) {
}
