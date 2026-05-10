package com.hi.locf.feature.locf.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hi.locf.feature.locf.dto.LocfBatchHistoryItemResponse;
import com.hi.locf.feature.locf.dto.LocfBatchRunRequest;
import com.hi.locf.feature.locf.dto.LocfBatchRunResponse;
import com.hi.locf.feature.locf.dto.LocfBatchStepItemResponse;
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
            // 같은 배치실행ID로 남아 있을 수 있는 중간/결과 데이터를 정리한다.
            locfTargetContractMapper.deleteTargetContractsByBatchExecutionId(execution.getBatchExecutionId());
            locfCashflowMapper.deleteCashflowBaseByBatchExecutionId(execution.getBatchExecutionId());
            locfEirMapper.deleteEirResultsByBatchExecutionId(execution.getBatchExecutionId());
            locfAmortizationMapper.deleteAmortizationDetailsByBatchExecutionId(execution.getBatchExecutionId());
            locfAmortizationMapper.deleteResultHeadersByBatchExecutionId(execution.getBatchExecutionId());
            locfAmortizationMapper.deleteResultSummaryByBatchExecutionId(execution.getBatchExecutionId());

            long targetCount = executeCountStep(
                    execution.getBatchExecutionId(),
                    "TARGET_CONTRACT",
                    () -> (long) locfTargetContractMapper.insertTargetContracts(execution.getBatchExecutionId(), request.getBaseDate())
            );
            // 이후 step 들은 이 대상계약 목록을 기준으로 반복 처리된다.
            List<LocfTargetContract> targets = locfTargetContractMapper.findTargetContractsByBatchExecutionId(execution.getBatchExecutionId());

            List<LocfCashflowBase> allCashflows = executeListStep(
                    execution.getBatchExecutionId(),
                    "CASHFLOW",
                    () -> {
                        List<LocfCashflowBase> generated = new ArrayList<>();
                        for (LocfTargetContract target : targets) {
                            // 원천 상환스케줄을 읽어 LOCF 계산용 약정 현금흐름으로 변환한다.
                            List<LocfCashflowBase> cashflows = locfSourceDataMapper.findRepaymentSchedulesByContractId(target.getContractId())
                                    .stream()
                                    .map(row -> LocfCashflowBase.create(execution.getBatchExecutionId(), target.getContractId(), target.getContractNo(), row))
                                    .toList();
                            for (LocfCashflowBase cashflow : cashflows) {
                                locfCashflowMapper.insertCashflowBase(cashflow);
                                generated.add(cashflow);
                            }
                        }
                        return generated;
                    }
            );

            List<LocfEirResult> eirResults = executeListStep(
                    execution.getBatchExecutionId(),
                    "EIR",
                    () -> {
                        List<LocfEirResult> results = new ArrayList<>();
                        for (LocfTargetContract target : targets) {
                            // 계약별 현금흐름 현재가치가 최초 장부가와 일치하도록 월 EIR을 역산한다.
                            List<LocfCashflowBase> contractCashflows = allCashflows.stream()
                                    .filter(cashflow -> cashflow.getContractId().equals(target.getContractId()))
                                    .toList();
                            LocfEirResult eirResult = locfEirService.calculateEir(execution.getBatchExecutionId(), target, contractCashflows);
                            locfEirMapper.insertEirResult(eirResult);
                            results.add(eirResult);
                        }
                        return results;
                    }
            );

            long processedCount = executeCountStep(
                    execution.getBatchExecutionId(),
                    "AMORTIZATION",
                    () -> {
                        long count = 0L;
                        for (LocfTargetContract target : targets) {
                            // EIR 결과와 현금흐름을 이용해 회차별 상각 스케줄을 만든다.
                            LocfEirResult eirResult = eirResults.stream()
                                    .filter(result -> result.getContractId().equals(target.getContractId()))
                                    .findFirst()
                                    .orElseThrow();
                            List<LocfCashflowBase> contractCashflows = allCashflows.stream()
                                    .filter(cashflow -> cashflow.getContractId().equals(target.getContractId()))
                                    .toList();
                            List<LocfAmortizationDetail> details = locfAmortizationService.calculateAmortization(
                                    execution.getBatchExecutionId(),
                                    target,
                                    eirResult,
                                    contractCashflows
                            );
                            for (LocfAmortizationDetail detail : details) {
                                locfAmortizationMapper.insertAmortizationDetail(detail);
                            }
                            locfAmortizationMapper.insertResultHeader(toHeader(request.getBaseDate(), execution, target, eirResult, details));
                            count++;
                        }
                        return count;
                    }
            );

            executeCountStep(
                    execution.getBatchExecutionId(),
                    "RESULT_SUMMARY",
                    () -> (long) locfAmortizationMapper.insertResultSummary(execution.getBatchExecutionId())
            );
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
        return locfBatchControlMapper.findBatchHistoryByBaseDate(baseDate)
                .stream()
                .map(execution -> new LocfBatchHistoryItemResponse(
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
                ))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocfBatchStepItemResponse> getStepHistory(String batchRunNo) {
        return locfBatchControlMapper.findStepHistoryByBatchRunNo(batchRunNo)
                .stream()
                .map(step -> new LocfBatchStepItemResponse(
                        step.getStepName(),
                        step.getStatusCode(),
                        step.getProcessedCount() == null ? 0L : step.getProcessedCount(),
                        step.getStartedAt(),
                        step.getFinishedAt(),
                        step.getErrorMessage()
                ))
                .toList();
    }

    private LocfResultHeader toHeader(
            LocalDate baseDate,
            LocfBatchExecution execution,
            LocfTargetContract target,
            LocfEirResult eirResult,
            List<LocfAmortizationDetail> details
    ) {
        // 상세 회차 데이터를 계약 단위 집계값으로 모아 헤더 테이블에 저장한다.
        BigDecimal totalEffectiveInterest = details.stream()
                .map(LocfAmortizationDetail::getEffectiveInterestRevenue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalFeeAmortization = details.stream()
                .map(LocfAmortizationDetail::getFeeAmortizationAmt)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalCostAmortization = details.stream()
                .map(LocfAmortizationDetail::getCostAmortizationAmt)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
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

    private long executeCountStep(Long batchExecutionId, String stepName, Supplier<Long> supplier) {
        // step 시작/종료/실패 이력을 별도 테이블에 남겨 배치 추적성을 확보한다.
        LocfBatchStepExecution step = LocfBatchStepExecution.start(batchExecutionId, stepName);
        locfBatchControlMapper.insertStepExecution(step);
        try {
            long processedCount = supplier.get();
            step.complete(processedCount);
            locfBatchControlMapper.completeStepExecution(step);
            return processedCount;
        } catch (Exception exception) {
            String message = exception.getMessage();
            if (message != null && message.length() > 900) {
                message = message.substring(0, 900);
            }
            step.fail(step.getProcessedCount() == null ? 0L : step.getProcessedCount(), message);
            locfBatchControlMapper.failStepExecution(step);
            throw exception;
        }
    }

    private <T> List<T> executeListStep(Long batchExecutionId, String stepName, Supplier<List<T>> supplier) {
        // 목록 반환 step 은 결과 건수 자체를 step 처리건수로 사용한다.
        LocfBatchStepExecution step = LocfBatchStepExecution.start(batchExecutionId, stepName);
        locfBatchControlMapper.insertStepExecution(step);
        try {
            List<T> results = supplier.get();
            step.complete(results.size());
            locfBatchControlMapper.completeStepExecution(step);
            return results;
        } catch (Exception exception) {
            String message = exception.getMessage();
            if (message != null && message.length() > 900) {
                message = message.substring(0, 900);
            }
            step.fail(step.getProcessedCount() == null ? 0L : step.getProcessedCount(), message);
            locfBatchControlMapper.failStepExecution(step);
            throw exception;
        }
    }
}
