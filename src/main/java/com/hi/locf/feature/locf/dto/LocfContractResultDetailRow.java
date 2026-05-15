package com.hi.locf.feature.locf.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LocfContractResultDetailRow {

    private long installmentNo;
    private LocalDate paymentDate;
    private BigDecimal openingPrincipalBal;
    private BigDecimal openingCarryingAmt;
    private BigDecimal scheduledPaymentAmt;
    private BigDecimal scheduledPrincipalAmt;
    private BigDecimal scheduledInterestAmt;
    private BigDecimal effectiveInterestRevenue;
    private BigDecimal feeAmortizationAmt;
    private BigDecimal costAmortizationAmt;
    private BigDecimal netAmortizationAmt;
    private BigDecimal closingPrincipalBal;
    private BigDecimal closingCarryingAmt;

    public LocfContractResultDetailRow() {
    }

    public LocfContractResultDetailRow(long installmentNo, LocalDate paymentDate, BigDecimal openingPrincipalBal,
            BigDecimal openingCarryingAmt, BigDecimal scheduledPaymentAmt, BigDecimal scheduledPrincipalAmt,
            BigDecimal scheduledInterestAmt, BigDecimal effectiveInterestRevenue, BigDecimal feeAmortizationAmt,
            BigDecimal costAmortizationAmt, BigDecimal netAmortizationAmt, BigDecimal closingPrincipalBal,
            BigDecimal closingCarryingAmt) {
        this.installmentNo = installmentNo;
        this.paymentDate = paymentDate;
        this.openingPrincipalBal = openingPrincipalBal;
        this.openingCarryingAmt = openingCarryingAmt;
        this.scheduledPaymentAmt = scheduledPaymentAmt;
        this.scheduledPrincipalAmt = scheduledPrincipalAmt;
        this.scheduledInterestAmt = scheduledInterestAmt;
        this.effectiveInterestRevenue = effectiveInterestRevenue;
        this.feeAmortizationAmt = feeAmortizationAmt;
        this.costAmortizationAmt = costAmortizationAmt;
        this.netAmortizationAmt = netAmortizationAmt;
        this.closingPrincipalBal = closingPrincipalBal;
        this.closingCarryingAmt = closingCarryingAmt;
    }

    public long getInstallmentNo() {
        return installmentNo;
    }

    public void setInstallmentNo(long installmentNo) {
        this.installmentNo = installmentNo;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getOpeningPrincipalBal() {
        return openingPrincipalBal;
    }

    public void setOpeningPrincipalBal(BigDecimal openingPrincipalBal) {
        this.openingPrincipalBal = openingPrincipalBal;
    }

    public BigDecimal getOpeningCarryingAmt() {
        return openingCarryingAmt;
    }

    public void setOpeningCarryingAmt(BigDecimal openingCarryingAmt) {
        this.openingCarryingAmt = openingCarryingAmt;
    }

    public BigDecimal getScheduledPaymentAmt() {
        return scheduledPaymentAmt;
    }

    public void setScheduledPaymentAmt(BigDecimal scheduledPaymentAmt) {
        this.scheduledPaymentAmt = scheduledPaymentAmt;
    }

    public BigDecimal getScheduledPrincipalAmt() {
        return scheduledPrincipalAmt;
    }

    public void setScheduledPrincipalAmt(BigDecimal scheduledPrincipalAmt) {
        this.scheduledPrincipalAmt = scheduledPrincipalAmt;
    }

    public BigDecimal getScheduledInterestAmt() {
        return scheduledInterestAmt;
    }

    public void setScheduledInterestAmt(BigDecimal scheduledInterestAmt) {
        this.scheduledInterestAmt = scheduledInterestAmt;
    }

    public BigDecimal getEffectiveInterestRevenue() {
        return effectiveInterestRevenue;
    }

    public void setEffectiveInterestRevenue(BigDecimal effectiveInterestRevenue) {
        this.effectiveInterestRevenue = effectiveInterestRevenue;
    }

    public BigDecimal getFeeAmortizationAmt() {
        return feeAmortizationAmt;
    }

    public void setFeeAmortizationAmt(BigDecimal feeAmortizationAmt) {
        this.feeAmortizationAmt = feeAmortizationAmt;
    }

    public BigDecimal getCostAmortizationAmt() {
        return costAmortizationAmt;
    }

    public void setCostAmortizationAmt(BigDecimal costAmortizationAmt) {
        this.costAmortizationAmt = costAmortizationAmt;
    }

    public BigDecimal getNetAmortizationAmt() {
        return netAmortizationAmt;
    }

    public void setNetAmortizationAmt(BigDecimal netAmortizationAmt) {
        this.netAmortizationAmt = netAmortizationAmt;
    }

    public BigDecimal getClosingPrincipalBal() {
        return closingPrincipalBal;
    }

    public void setClosingPrincipalBal(BigDecimal closingPrincipalBal) {
        this.closingPrincipalBal = closingPrincipalBal;
    }

    public BigDecimal getClosingCarryingAmt() {
        return closingCarryingAmt;
    }

    public void setClosingCarryingAmt(BigDecimal closingCarryingAmt) {
        this.closingCarryingAmt = closingCarryingAmt;
    }
}
