package com.hi.locf.feature.provision.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ProvisionIndividualEvalTargetResponse {

	private Long individualTargetId;
	private LocalDate baseDate;
	private Long contractId;
	private String contractNo;
	private String customerName;
	private String productCode;
	private String evalReasonCode;
	private String evalReasonDetail;
	private BigDecimal recoveryExpectedAmt;
	private BigDecimal discountRate;
	private String activeYn;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public ProvisionIndividualEvalTargetResponse() {
	}

	public Long getIndividualTargetId() {
		return individualTargetId;
	}
	public void setIndividualTargetId(Long individualTargetId) {
		this.individualTargetId = individualTargetId;
	}
	public LocalDate getBaseDate() {
		return baseDate;
	}
	public void setBaseDate(LocalDate baseDate) {
		this.baseDate = baseDate;
	}
	public Long getContractId() {
		return contractId;
	}
	public void setContractId(Long contractId) {
		this.contractId = contractId;
	}
	public String getContractNo() {
		return contractNo;
	}
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
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
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
	public ProvisionIndividualEvalTargetResponse(Long individualTargetId, LocalDate baseDate, Long contractId,
			String contractNo, String customerName, String productCode, String evalReasonCode, String evalReasonDetail,
			BigDecimal recoveryExpectedAmt, BigDecimal discountRate, String activeYn, LocalDateTime createdAt,
			LocalDateTime updatedAt) {
		super();
		this.individualTargetId = individualTargetId;
		this.baseDate = baseDate;
		this.contractId = contractId;
		this.contractNo = contractNo;
		this.customerName = customerName;
		this.productCode = productCode;
		this.evalReasonCode = evalReasonCode;
		this.evalReasonDetail = evalReasonDetail;
		this.recoveryExpectedAmt = recoveryExpectedAmt;
		this.discountRate = discountRate;
		this.activeYn = activeYn;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

		
}
