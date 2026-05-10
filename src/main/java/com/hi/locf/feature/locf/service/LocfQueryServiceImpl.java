package com.hi.locf.feature.locf.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hi.locf.common.code.ErrorCode;
import com.hi.locf.common.exception.BusinessException;
import com.hi.locf.feature.locf.dto.LocfContractResultDetailRow;
import com.hi.locf.feature.locf.dto.LocfContractResultHeaderResponse;
import com.hi.locf.feature.locf.dto.LocfContractResultResponse;
import com.hi.locf.feature.locf.dto.LocfSourceContractItemResponse;
import com.hi.locf.feature.locf.dto.LocfSummaryItemResponse;
import com.hi.locf.feature.locf.entity.LoanContractSource;
import com.hi.locf.feature.locf.entity.LocfResultHeader;
import com.hi.locf.feature.locf.mapper.LocfQueryMapper;

@Service
public class LocfQueryServiceImpl implements LocfQueryService {

    private final LocfQueryMapper locfQueryMapper;

    public LocfQueryServiceImpl(LocfQueryMapper locfQueryMapper) {
        this.locfQueryMapper = locfQueryMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public LocfContractResultResponse getContractResult(String contractNo) {
        // 계약번호 기준 최신 배치 결과 헤더를 찾는다.
        LocfResultHeader header = locfQueryMapper.findLatestResultHeaderByContractNo(contractNo);
        if (header == null) {
            throw new BusinessException(ErrorCode.LOCF_RESULT_NOT_FOUND);
        }

        // 상세 회차 결과는 화면이 바로 바인딩하기 쉬운 DTO 형태로 변환한다.
        List<LocfContractResultDetailRow> details = locfQueryMapper.findLatestResultDetailsByContractNo(contractNo)
                .stream()
                .map(detail -> new LocfContractResultDetailRow(
                        detail.getInstallmentNo() == null ? 0L : detail.getInstallmentNo(),
                        detail.getPaymentDate(),
                        detail.getOpeningPrincipalBal(),
                        detail.getOpeningCarryingAmt(),
                        detail.getScheduledPaymentAmt(),
                        detail.getScheduledPrincipalAmt(),
                        detail.getScheduledInterestAmt(),
                        detail.getEffectiveInterestRevenue(),
                        detail.getFeeAmortizationAmt(),
                        detail.getCostAmortizationAmt(),
                        detail.getNetAmortizationAmt(),
                        detail.getClosingPrincipalBal(),
                        detail.getClosingCarryingAmt()
                ))
                .toList();

        LocfContractResultHeaderResponse responseHeader = new LocfContractResultHeaderResponse(
                header.getBatchRunNo(),
                header.getBaseDate(),
                header.getContractNo(),
                header.getCustomerName(),
                header.getProductCode(),
                header.getPrincipalAmt(),
                header.getInitialCarryingAmount(),
                header.getAnnualEir(),
                header.getFinalCarryingAmount(),
                header.getTotalEffectiveInterestRev(),
                header.getTotalFeeAmortizationAmt(),
                header.getTotalCostAmortizationAmt()
        );

        return new LocfContractResultResponse(responseHeader, details);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocfSummaryItemResponse> getSummary(LocalDate baseDate) {
        // 기준일자별 상품 단위 요약 결과를 조회한다.
        return locfQueryMapper.findSummaryByBaseDate(baseDate)
                .stream()
                .map(summary -> new LocfSummaryItemResponse(
                        summary.getBaseDate(),
                        summary.getProductCode(),
                        summary.getContractCount() == null ? 0L : summary.getContractCount(),
                        summary.getTotalInitialCarryingAmt(),
                        summary.getTotalFinalCarryingAmt(),
                        summary.getTotalEffectiveInterestRev()
                ))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocfSourceContractItemResponse> getSourceContracts() {
        // LOCF 원천계약 현황을 학습용 화면에서 바로 확인할 수 있게 제공한다.
        return locfQueryMapper.findSourceContracts()
                .stream()
                .map(this::toSourceContractResponse)
                .toList();
    }

    private LocfSourceContractItemResponse toSourceContractResponse(LoanContractSource contract) {
        return new LocfSourceContractItemResponse(
                contract.getContractNo(),
                contract.getCustomerName(),
                contract.getProductCode(),
                contract.getRepaymentType(),
                contract.getExecutionDate(),
                contract.getMaturityDate(),
                contract.getPrincipalAmt(),
                contract.getAnnualNominalRate(),
                contract.getOutstandingPrincipalAmt()
        );
    }
}
