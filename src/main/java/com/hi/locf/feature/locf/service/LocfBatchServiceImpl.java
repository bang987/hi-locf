package com.hi.locf.feature.locf.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hi.locf.common.exception.BusinessException;
import com.hi.locf.common.code.ErrorCode;
import com.hi.locf.feature.locf.dto.LocfBatchHistoryItemResponse;
import com.hi.locf.feature.locf.dto.LocfBatchRunRequest;
import com.hi.locf.feature.locf.dto.LocfBatchRunResponse;
import com.hi.locf.feature.locf.dto.LocfBatchStepItemResponse;
import com.hi.locf.feature.locf.entity.LoanRepaymentScheduleSource;
import com.hi.locf.feature.locf.entity.LocfAmortizationDetail;
import com.hi.locf.feature.locf.entity.LocfBatchExecution;
import com.hi.locf.feature.locf.entity.LocfBatchStepExecution;
import com.hi.locf.feature.locf.entity.LocfCashflowBase;
import com.hi.locf.feature.locf.entity.LocfEirResult;
import com.hi.locf.feature.locf.entity.LocfResultHeader;
import com.hi.locf.feature.locf.entity.LocfTargetContract;
import com.hi.locf.feature.locf.mapper.LocfBatchControlMapper;
import com.hi.locf.feature.locf.mapper.LocfAmortizationMapper;
import com.hi.locf.feature.locf.mapper.LocfCashflowMapper;
import com.hi.locf.feature.locf.mapper.LocfEirMapper;
import com.hi.locf.feature.locf.mapper.LocfSourceDataMapper;
import com.hi.locf.feature.locf.mapper.LocfTargetContractMapper;

@Service
public class LocfBatchServiceImpl implements LocfBatchService {

    // 배치실행번호는 사람이 보기 쉽도록 일시 기반으로 만든다.
    private static final DateTimeFormatter BATCH_NO_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final LocfBatchControlMapper locfBatchControlMapper;
    private final LocfTargetContractMapper locfTargetContractMapper;
    private final LocfSourceDataMapper locfSourceDataMapper;
    private final LocfCashflowMapper locfCashflowMapper;
    private final LocfEirMapper locfEirMapper;
    private final LocfAmortizationMapper locfAmortizationMapper;
    private final LocfEirService locfEirService;
    private final LocfAmortizationService locfAmortizationService;

    public LocfBatchServiceImpl(
            LocfBatchControlMapper locfBatchControlMapper,
            LocfTargetContractMapper locfTargetContractMapper,
            LocfSourceDataMapper locfSourceDataMapper,
            LocfCashflowMapper locfCashflowMapper,
            LocfEirMapper locfEirMapper,
            LocfAmortizationMapper locfAmortizationMapper,
            LocfEirService locfEirService,
            LocfAmortizationService locfAmortizationService
    ) {
        this.locfBatchControlMapper = locfBatchControlMapper;
        this.locfTargetContractMapper = locfTargetContractMapper;
        this.locfSourceDataMapper = locfSourceDataMapper;
        this.locfCashflowMapper = locfCashflowMapper;
        this.locfEirMapper = locfEirMapper;
        this.locfAmortizationMapper = locfAmortizationMapper;
        this.locfEirService = locfEirService;
        this.locfAmortizationService = locfAmortizationService;
    }

    @Override
    @Transactional
    public LocfBatchRunResponse runBatch(LocfBatchRunRequest request) {
        // 배치 실행 1건에 대한 헤더를 먼저 만든다.
        String batchRunNo = "LOCF-" + LocalDateTime.now().format(BATCH_NO_FORMAT);
        String batchType = request.getBatchType() == null || request.getBatchType().isBlank()
                ? "MANUAL"
                : request.getBatchType();

        LocfBatchExecution execution = LocfBatchExecution.start(batchRunNo, request.getBaseDate(), batchType);
        locfBatchControlMapper.insertBatchExecution(execution);

        try {
            clearBatchData(execution.getBatchExecutionId());

            long targetCount = createTargetContractsStep(execution.getBatchExecutionId(), request.getBaseDate());
            // 이후 step 들은 이 대상계약 목록을 기준으로 반복 처리된다.
            List<LocfTargetContract> targets = loadTargetContracts(execution.getBatchExecutionId());
            Map<Long, List<LocfCashflowBase>> cashflowMap = createCashflowStep(execution.getBatchExecutionId(), targets);
            Map<Long, LocfEirResult> eirResultMap = createEirStep(execution.getBatchExecutionId(), targets, cashflowMap);
            long processedCount = createAmortizationStep(
                    execution.getBatchExecutionId(),
                    request.getBaseDate(),
                    execution,
                    targets,
                    cashflowMap,
                    eirResultMap
            );
            createSummaryStep(execution.getBatchExecutionId());
            // 모든 step이 정상 종료되면 배치 헤더를 완료 상태로 마감한다.
            execution.complete(targetCount, processedCount, 0L);
            locfBatchControlMapper.completeBatchExecution(execution);
        } catch (Exception exception) {
            String message = exception.getMessage();
            if (message != null && message.length() > 900) {
                message = message.substring(0, 900);
            }
            execution.fail(message);
            locfBatchControlMapper.failBatchExecution(execution);
            throw exception;
        }

        return new LocfBatchRunResponse(
                execution.getBatchRunNo(),
                execution.getBaseDate(),
                execution.getStatusCode(),
                execution.getTargetCount(),
                execution.getProcessedCount(),
                execution.getErrorCount(),
                execution.getStartedAt(),
                execution.getFinishedAt()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocfBatchHistoryItemResponse> getBatchHistory(LocalDate baseDate) {
        // 화면에서는 엔티티를 그대로 쓰지 않고 응답 DTO로 변환해 내려준다.
        List<LocfBatchExecution> executions = locfBatchControlMapper.findBatchHistoryByBaseDate(baseDate);
        List<LocfBatchHistoryItemResponse> responses = new ArrayList<>();
        for (LocfBatchExecution execution : executions) {
            responses.add(new LocfBatchHistoryItemResponse(
                    execution.getBatchRunNo(),
                    execution.getBaseDate(),
                    execution.getBatchType(),
                    execution.getStatusCode(),
                    execution.getTargetCount() == null ? 0L : execution.getTargetCount(),
                    execution.getProcessedCount() == null ? 0L : execution.getProcessedCount(),
                    execution.getErrorCount() == null ? 0L : execution.getErrorCount(),
                    execution.getStartedAt(),
                    execution.getFinishedAt(),
                    execution.getErrorMessage()
            ));
        }
        return responses;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocfBatchStepItemResponse> getStepHistory(String batchRunNo) {
        List<LocfBatchStepExecution> stepExecutions = locfBatchControlMapper.findStepHistoryByBatchRunNo(batchRunNo);
        List<LocfBatchStepItemResponse> responses = new ArrayList<>();
        for (LocfBatchStepExecution step : stepExecutions) {
            responses.add(new LocfBatchStepItemResponse(
                    step.getStepName(),
                    step.getStatusCode(),
                    step.getProcessedCount() == null ? 0L : step.getProcessedCount(),
                    step.getStartedAt(),
                    step.getFinishedAt(),
                    step.getErrorMessage()
            ));
        }
        return responses;
    }

    private LocfResultHeader toHeader(
            LocalDate baseDate,
            LocfBatchExecution execution,
            LocfTargetContract target,
            LocfEirResult eirResult,
            List<LocfAmortizationDetail> details
    ) {
        // 상세 회차 데이터를 계약 단위 집계값으로 모아 헤더 테이블에 저장한다.
        BigDecimal totalEffectiveInterest = BigDecimal.ZERO;
        BigDecimal totalFeeAmortization = BigDecimal.ZERO;
        BigDecimal totalCostAmortization = BigDecimal.ZERO;

        for (LocfAmortizationDetail detail : details) {
            totalEffectiveInterest = totalEffectiveInterest.add(detail.getEffectiveInterestRevenue());
            totalFeeAmortization = totalFeeAmortization.add(detail.getFeeAmortizationAmt());
            totalCostAmortization = totalCostAmortization.add(detail.getCostAmortizationAmt());
        }

        BigDecimal finalCarryingAmount = details.isEmpty()
                ? eirResult.getInitialCarryingAmount()
                : details.get(details.size() - 1).getClosingCarryingAmt();

        LocfResultHeader header = new LocfResultHeader();
        header.setBatchExecutionId(execution.getBatchExecutionId());
        header.setBatchRunNo(execution.getBatchRunNo());
        header.setBaseDate(baseDate);
        header.setContractId(target.getContractId());
        header.setContractNo(target.getContractNo());
        header.setCustomerId(target.getCustomerId());
        header.setCustomerName(target.getCustomerName());
        header.setProductCode(target.getProductCode());
        header.setPrincipalAmt(target.getPrincipalAmt());
        header.setInitialCarryingAmount(eirResult.getInitialCarryingAmount());
        header.setAnnualEir(eirResult.getAnnualEir());
        header.setFinalCarryingAmount(finalCarryingAmount);
        header.setTotalEffectiveInterestRev(totalEffectiveInterest);
        header.setTotalFeeAmortizationAmt(totalFeeAmortization);
        header.setTotalCostAmortizationAmt(totalCostAmortization);
        return header;
    }

    private void clearBatchData(Long batchExecutionId) {
        // 배치 재실행을 위해 중간/결과 데이터를 DB 프로시저로 한 번에 정리한다.
        locfBatchControlMapper.clearBatchData(batchExecutionId);
    }

    private long createTargetContractsStep(Long batchExecutionId, LocalDate baseDate) {
        LocfBatchStepExecution step = LocfBatchStepExecution.start(batchExecutionId, "TARGET_CONTRACT");
        locfBatchControlMapper.insertStepExecution(step);

        try {
            locfTargetContractMapper.insertTargetContracts(batchExecutionId, baseDate);
            long processedCount = locfTargetContractMapper.countTargetContracts(batchExecutionId);
            step.complete(processedCount);
            locfBatchControlMapper.completeStepExecution(step);
            return processedCount;
        } catch (Exception exception) {
            failStep(step, exception);
            throw exception;
        }
    }

    private List<LocfTargetContract> loadTargetContracts(Long batchExecutionId) {
        return locfTargetContractMapper.findTargetContractsByBatchExecutionId(batchExecutionId);
    }

    private Map<Long, List<LocfCashflowBase>> createCashflowStep(Long batchExecutionId, List<LocfTargetContract> targets) {
        LocfBatchStepExecution step = LocfBatchStepExecution.start(batchExecutionId, "CASHFLOW");
        locfBatchControlMapper.insertStepExecution(step);

        try {
            Map<Long, List<LocfCashflowBase>> cashflowMap = new LinkedHashMap<>();
            long processedCount = 0L;

            for (LocfTargetContract target : targets) {
                List<LocfCashflowBase> cashflows = buildCashflowsForContract(batchExecutionId, target);
                cashflowMap.put(target.getContractId(), cashflows);

                for (LocfCashflowBase cashflow : cashflows) {
                    locfCashflowMapper.insertCashflowBase(cashflow);
                    processedCount++;
                }
            }

            step.complete(processedCount);
            locfBatchControlMapper.completeStepExecution(step);
            return cashflowMap;
        } catch (Exception exception) {
            failStep(step, exception);
            throw exception;
        }
    }

    private Map<Long, LocfEirResult> createEirStep(
            Long batchExecutionId,
            List<LocfTargetContract> targets,
            Map<Long, List<LocfCashflowBase>> cashflowMap
    ) {
        LocfBatchStepExecution step = LocfBatchStepExecution.start(batchExecutionId, "EIR");
        locfBatchControlMapper.insertStepExecution(step);

        try {
            Map<Long, LocfEirResult> eirResultMap = new LinkedHashMap<>();
            long processedCount = 0L;

            for (LocfTargetContract target : targets) {
                List<LocfCashflowBase> contractCashflows = getCashflowsByContractId(cashflowMap, target.getContractId());
                LocfEirResult eirResult = locfEirService.calculateEir(batchExecutionId, target, contractCashflows);
                locfEirMapper.insertEirResult(eirResult);
                eirResultMap.put(target.getContractId(), eirResult);
                processedCount++;
            }

            step.complete(processedCount);
            locfBatchControlMapper.completeStepExecution(step);
            return eirResultMap;
        } catch (Exception exception) {
            failStep(step, exception);
            throw exception;
        }
    }

    private long createAmortizationStep(
            Long batchExecutionId,
            LocalDate baseDate,
            LocfBatchExecution execution,
            List<LocfTargetContract> targets,
            Map<Long, List<LocfCashflowBase>> cashflowMap,
            Map<Long, LocfEirResult> eirResultMap
    ) {
        LocfBatchStepExecution step = LocfBatchStepExecution.start(batchExecutionId, "AMORTIZATION");
        locfBatchControlMapper.insertStepExecution(step);

        try {
            long processedCount = 0L;

            for (LocfTargetContract target : targets) {
                LocfEirResult eirResult = getEirResultByContractId(eirResultMap, target.getContractId());
                List<LocfCashflowBase> contractCashflows = getCashflowsByContractId(cashflowMap, target.getContractId());
                List<LocfAmortizationDetail> details = locfAmortizationService.calculateAmortization(
                        batchExecutionId,
                        target,
                        eirResult,
                        contractCashflows
                );

                for (LocfAmortizationDetail detail : details) {
                    locfAmortizationMapper.insertAmortizationDetail(detail);
                }

                locfAmortizationMapper.insertResultHeader(toHeader(baseDate, execution, target, eirResult, details));
                processedCount++;
            }

            step.complete(processedCount);
            locfBatchControlMapper.completeStepExecution(step);
            return processedCount;
        } catch (Exception exception) {
            failStep(step, exception);
            throw exception;
        }
    }

    private long createSummaryStep(Long batchExecutionId) {
        LocfBatchStepExecution step = LocfBatchStepExecution.start(batchExecutionId, "RESULT_SUMMARY");
        locfBatchControlMapper.insertStepExecution(step);

        try {
            locfAmortizationMapper.insertResultSummary(batchExecutionId);
            long processedCount = locfAmortizationMapper.countResultSummary(batchExecutionId);
            step.complete(processedCount);
            locfBatchControlMapper.completeStepExecution(step);
            return processedCount;
        } catch (Exception exception) {
            failStep(step, exception);
            throw exception;
        }
    }

    private List<LocfCashflowBase> buildCashflowsForContract(Long batchExecutionId, LocfTargetContract target) {
        List<LocfCashflowBase> generated = new ArrayList<>();
        List<LoanRepaymentScheduleSource> repaymentSchedules =
                locfSourceDataMapper.findRepaymentSchedulesByContractId(target.getContractId());

        for (LoanRepaymentScheduleSource repaymentSchedule : repaymentSchedules) {
            generated.add(LocfCashflowBase.create(
                    batchExecutionId,
                    target.getContractId(),
                    target.getContractNo(),
                    repaymentSchedule
            ));
        }

        return generated;
    }

    private List<LocfCashflowBase> getCashflowsByContractId(
            Map<Long, List<LocfCashflowBase>> cashflowMap,
            Long contractId
    ) {
        List<LocfCashflowBase> cashflows = cashflowMap.get(contractId);
        if (cashflows == null || cashflows.isEmpty()) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return cashflows;
    }

    private LocfEirResult getEirResultByContractId(Map<Long, LocfEirResult> eirResultMap, Long contractId) {
        LocfEirResult eirResult = eirResultMap.get(contractId);
        if (eirResult == null) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return eirResult;
    }

    private void failStep(LocfBatchStepExecution step, Exception exception) {
        String message = exception.getMessage();
        if (message != null && message.length() > 900) {
            message = message.substring(0, 900);
        }
        step.fail(step.getProcessedCount() == null ? 0L : step.getProcessedCount(), message);
        locfBatchControlMapper.failStepExecution(step);
    }
}
