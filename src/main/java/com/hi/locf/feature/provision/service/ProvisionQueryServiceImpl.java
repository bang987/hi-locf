package com.hi.locf.feature.provision.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hi.locf.common.code.ErrorCode;
import com.hi.locf.common.exception.BusinessException;
import com.hi.locf.feature.provision.dto.ProvisionContractResultRow;
import com.hi.locf.feature.provision.dto.ProvisionEclCashflowDetailResponse;
import com.hi.locf.feature.provision.dto.ProvisionSegmentSummaryItemResponse;
import com.hi.locf.feature.provision.dto.ProvisionSummaryItemResponse;
import com.hi.locf.feature.provision.entity.ProvisionContractResultDetail;
import com.hi.locf.feature.provision.entity.ProvisionResultSummary;
import com.hi.locf.feature.provision.mapper.ProvisionQueryMapper;

@Service
public class ProvisionQueryServiceImpl implements ProvisionQueryService {

    private final ProvisionQueryMapper provisionQueryMapper;

    public ProvisionQueryServiceImpl(ProvisionQueryMapper provisionQueryMapper) {
        this.provisionQueryMapper = provisionQueryMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProvisionContractResultRow> getContractResult(String contractNo) {
        List<ProvisionContractResultDetail> details = provisionQueryMapper.findLatestContractResultsByContractNo(contractNo);
        if (details.isEmpty()) {
            throw new BusinessException(ErrorCode.PROVISION_RESULT_NOT_FOUND);
        }

        List<ProvisionContractResultRow> responses = new ArrayList<>();
        for (ProvisionContractResultDetail detail : details) {
            responses.add(new ProvisionContractResultRow(
                    detail.getContractNo(),
                    detail.getCustomerName(),
                    detail.getProductCode(),
                    detail.getStageCode(),
                    detail.getStageReason(),
                    detail.getCarryingAmount(),
                    detail.getEadAmount(),
                    detail.getOneYearPdRate(),
                    detail.getLifetimePdRate(),
                    detail.getLgdRate(),
                    detail.getRecoveryRate(),
                    detail.getEclAmount()
            ));
        }
        return responses;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProvisionSummaryItemResponse> getSummary(LocalDate baseDate) {
        List<ProvisionSummaryItemResponse> responses = new ArrayList<>();
        for (ProvisionResultSummary summary : provisionQueryMapper.findSummaryByBaseDate(baseDate)) {
            responses.add(new ProvisionSummaryItemResponse(
                    summary.getBaseDate(),
                    summary.getProductCode(),
                    summary.getStageCode(),
                    summary.getContractCount() == null ? 0L : summary.getContractCount(),
                    summary.getTotalCarryingAmount(),
                    summary.getTotalEadAmount(),
                    summary.getTotalEclAmount()
            ));
        }
        return responses;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProvisionSegmentSummaryItemResponse> getSegmentSummary(LocalDate baseDate) {
        return provisionQueryMapper.findSegmentSummaryByBaseDate(baseDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProvisionEclCashflowDetailResponse> getCashflows(String contractNo) {
        List<ProvisionEclCashflowDetailResponse> responses =
                provisionQueryMapper.findEclCashflowDetailByContractNo(contractNo);

        if (responses.isEmpty()) {
            throw new BusinessException(ErrorCode.PROVISION_RESULT_NOT_FOUND);
        }

        return responses;
    }
}
