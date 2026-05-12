package com.hi.locf.feature.locf.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class LoanCollateralSource {

    private Long collateralId;
    private Long contractId;
    private LocalDate baseDate;
    private String collateralType;
    private BigDecimal collateralValueAmt;
    private BigDecimal recoveryRate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static LoanCollateralSource create(
            Long contractId,
            LocalDate baseDate,
            String collateralType,
            BigDecimal collateralValueAmt,
            BigDecimal recoveryRate
    ) {
        LocalDateTime now = LocalDateTime.now();
        LoanCollateralSource collateral = new LoanCollateralSource();
        collateral.contractId = contractId;
        collateral.baseDate = baseDate;
        collateral.collateralType = collateralType;
        collateral.collateralValueAmt = collateralValueAmt;
        collateral.recoveryRate = recoveryRate;
        collateral.createdAt = now;
        collateral.updatedAt = now;
        return collateral;
    }

    public Long getCollateralId() { return collateralId; }
    public void setCollateralId(Long collateralId) { this.collateralId = collateralId; }
    public Long getContractId() { return contractId; }
    public void setContractId(Long contractId) { this.contractId = contractId; }
    public LocalDate getBaseDate() { return baseDate; }
    public void setBaseDate(LocalDate baseDate) { this.baseDate = baseDate; }
    public String getCollateralType() { return collateralType; }
    public void setCollateralType(String collateralType) { this.collateralType = collateralType; }
    public BigDecimal getCollateralValueAmt() { return collateralValueAmt; }
    public void setCollateralValueAmt(BigDecimal collateralValueAmt) { this.collateralValueAmt = collateralValueAmt; }
    public BigDecimal getRecoveryRate() { return recoveryRate; }
    public void setRecoveryRate(BigDecimal recoveryRate) { this.recoveryRate = recoveryRate; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
