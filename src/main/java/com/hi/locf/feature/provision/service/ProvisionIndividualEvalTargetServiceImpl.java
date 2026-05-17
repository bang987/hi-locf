package com.hi.locf.feature.provision.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.hi.locf.common.code.ErrorCode;
import com.hi.locf.common.exception.BusinessException;
import com.hi.locf.feature.locf.entity.LoanContractSource;
import com.hi.locf.feature.locf.mapper.LocfSourceDataMapper;
import com.hi.locf.feature.provision.dto.ProvisionIndividualEvalTargetRequest;
import com.hi.locf.feature.provision.dto.ProvisionIndividualEvalTargetResponse;
import com.hi.locf.feature.provision.entity.ProvisionIndividualEvalTarget;
import com.hi.locf.feature.provision.mapper.ProvisionIndividualEvalTargetMapper;

@Service
@Transactional
public class ProvisionIndividualEvalTargetServiceImpl implements ProvisionIndividualEvalTargetService {

    private final ProvisionIndividualEvalTargetMapper provisionIndividualEvalTargetMapper;
    private final LocfSourceDataMapper locfSourceDataMapper;

    public ProvisionIndividualEvalTargetServiceImpl(
            ProvisionIndividualEvalTargetMapper provisionIndividualEvalTargetMapper,
            LocfSourceDataMapper locfSourceDataMapper
    ) {
        this.provisionIndividualEvalTargetMapper = provisionIndividualEvalTargetMapper;
        this.locfSourceDataMapper = locfSourceDataMapper;
    }

    @Override
    public ProvisionIndividualEvalTargetResponse createTarget(ProvisionIndividualEvalTargetRequest request) {
    	
        validateCreateRequest(request);//파라미터값 검증

        LoanContractSource contract = getContractByContractNo(request.getContractNo());
        
        assertNoDuplicate(request.getBaseDate(), contract.getContractId());

        ProvisionIndividualEvalTarget target = createEntity(request, contract);

        provisionIndividualEvalTargetMapper.insertTarget(target);

        return toResponse(target);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProvisionIndividualEvalTargetResponse> getTargets(LocalDate baseDate, String contractNo) {
        if (baseDate == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "기준일자는 필수입니다.");
        }

        List<ProvisionIndividualEvalTargetResponse> responses = new ArrayList<>();
        List<ProvisionIndividualEvalTarget> targets =
                provisionIndividualEvalTargetMapper.findTargets(baseDate, contractNo);

        for (ProvisionIndividualEvalTarget target : targets) {
            responses.add(toResponse(target));
        }

        return responses;
    }

    @Override
    public void deleteTarget(Long individualTargetId) {
        if (individualTargetId == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "개별평가 대상 ID는 필수입니다.");
        }

        ProvisionIndividualEvalTarget target =
                provisionIndividualEvalTargetMapper.findTargetById(individualTargetId);

        if (target == null) {
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "개별평가 대상을 찾을 수 없습니다.");
        }

        provisionIndividualEvalTargetMapper.deleteTargetById(individualTargetId);
    }

    private void validateCreateRequest(ProvisionIndividualEvalTargetRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "요청값이 없습니다.");
        }
        if (request.getBaseDate() == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "기준일자는 필수입니다.");
        }
        if (!StringUtils.hasText(request.getContractNo())) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "계약번호는 필수입니다.");
        }
        if (!StringUtils.hasText(request.getEvalReasonCode())) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "평가사유코드는 필수입니다.");
        }
    }

    private LoanContractSource getContractByContractNo(String contractNo) {
        LoanContractSource contract = locfSourceDataMapper.findLoanContractByContractNo(contractNo);

        if (contract == null) {
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "계약번호에 해당하는 계약이 없습니다.");
        }

        return contract;
    }

    private void assertNoDuplicate(LocalDate baseDate, Long contractId) {
        ProvisionIndividualEvalTarget duplicate =
                provisionIndividualEvalTargetMapper.findDuplicateTarget(baseDate, contractId);

        if (duplicate != null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "이미 등록된 개별평가 대상입니다.");
        }
    }

    private ProvisionIndividualEvalTarget createEntity(
            ProvisionIndividualEvalTargetRequest request,
            LoanContractSource contract
    ) {
        LocalDateTime now = LocalDateTime.now();
        ProvisionIndividualEvalTarget target = new ProvisionIndividualEvalTarget();
        target.setBaseDate(request.getBaseDate());
        target.setContractId(contract.getContractId());
        target.setContractNo(contract.getContractNo());
        target.setCustomerName(contract.getCustomerName());
        target.setProductCode(contract.getProductCode());
        target.setEvalReasonCode(request.getEvalReasonCode());
        target.setEvalReasonDetail(request.getEvalReasonDetail());
        target.setRecoveryExpectedAmt(request.getRecoveryExpectedAmt());
        target.setDiscountRate(request.getDiscountRate());
        target.setActiveYn(StringUtils.hasText(request.getActiveYn()) ? request.getActiveYn() : "Y");
        target.setCreatedAt(now);
        target.setUpdatedAt(now);
        return target;
    }

    private ProvisionIndividualEvalTargetResponse toResponse(ProvisionIndividualEvalTarget target) {
        ProvisionIndividualEvalTargetResponse response = new ProvisionIndividualEvalTargetResponse();
        response.setIndividualTargetId(target.getIndividualTargetId());
        response.setBaseDate(target.getBaseDate());
        response.setContractId(target.getContractId());
        response.setContractNo(target.getContractNo());
        response.setCustomerName(target.getCustomerName());
        response.setProductCode(target.getProductCode());
        response.setEvalReasonCode(target.getEvalReasonCode());
        response.setEvalReasonDetail(target.getEvalReasonDetail());
        response.setRecoveryExpectedAmt(target.getRecoveryExpectedAmt());
        response.setDiscountRate(target.getDiscountRate());
        response.setActiveYn(target.getActiveYn());
        response.setCreatedAt(target.getCreatedAt());
        response.setUpdatedAt(target.getUpdatedAt());
        return response;
    }
}
