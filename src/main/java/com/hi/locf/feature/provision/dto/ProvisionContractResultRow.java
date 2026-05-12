package com.hi.locf.feature.provision.dto;

import java.math.BigDecimal;

public record ProvisionContractResultRow(
        String contractNo,
        String customerName,
        String productCode,
        String stageCode,
        String stageReason,
        BigDecimal carryingAmount,
        BigDecimal eadAmount,
        BigDecimal oneYearPdRate,
        BigDecimal lifetimePdRate,
        BigDecimal lgdRate,
        BigDecimal recoveryRate,
        BigDecimal eclAmount
) {
}
