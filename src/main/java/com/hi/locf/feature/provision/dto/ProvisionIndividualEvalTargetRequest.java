package com.hi.locf.feature.provision.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProvisionIndividualEvalTargetRequest {
    private LocalDate baseDate;
    private String contractNo;
    private String evalReasonCode;
    private String evalReasonDetail;
    private BigDecimal recoveryExpectedAmt;
    private BigDecimal discountRate;
    private String activeYn;

    public ProvisionIndividualEvalTargetRequest() {
    }

    public ProvisionIndividualEvalTargetRequest(LocalDate baseDate, String contractNo, String evalReasonCode,
            String evalReasonDetail, BigDecimal recoveryExpectedAmt, BigDecimal discountRate, String activeYn) {
        this.baseDate = baseDate;
        this.contractNo = contractNo;
        this.evalReasonCode = evalReasonCode;
        this.evalReasonDetail = evalReasonDetail;
        this.recoveryExpectedAmt = recoveryExpectedAmt;
        this.discountRate = discountRate;
        this.activeYn = activeYn;
    }

    public LocalDate getBaseDate() {
        return baseDate;
    }

    public void setBaseDate(LocalDate baseDate) {
        this.baseDate = baseDate;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getEvalReasonCode() {
        return evalReasonCode;
    }

    public void setEvalReasonCode(String evalReasonCode) {
        this.evalReasonCode = evalReasonCode;
    }

    public String getEvalReasonDetail() {
        return evalReasonDetail;
    }

    public void setEvalReasonDetail(String evalReasonDetail) {
        this.evalReasonDetail = evalReasonDetail;
    }

    public BigDecimal getRecoveryExpectedAmt() {
        return recoveryExpectedAmt;
    }

    public void setRecoveryExpectedAmt(BigDecimal recoveryExpectedAmt) {
        this.recoveryExpectedAmt = recoveryExpectedAmt;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    public String getActiveYn() {
        return activeYn;
    }

    public void setActiveYn(String activeYn) {
        this.activeYn = activeYn;
    }

}
