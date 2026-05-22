package com.hi.locf.feature.provision.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hi.locf.common.code.ErrorCode;
import com.hi.locf.common.exception.BusinessException;
import com.hi.locf.feature.provision.dto.ProvisionBatchHistoryItemResponse;
import com.hi.locf.feature.provision.dto.ProvisionBatchRunRequest;
import com.hi.locf.feature.provision.dto.ProvisionBatchRunResponse;
import com.hi.locf.feature.provision.dto.ProvisionBatchStepItemResponse;
import com.hi.locf.feature.provision.entity.ProvisionBatchExecution;
import com.hi.locf.feature.provision.entity.ProvisionBatchStepExecution;
import com.hi.locf.feature.provision.mapper.ProvisionBatchControlMapper;
import com.hi.locf.feature.provision.mapper.ProvisionCalculationMapper;

@Service
public class ProvisionBatchServiceImpl implements ProvisionBatchService {

    private static final DateTimeFormatter BATCH_NO_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final ProvisionBatchControlMapper provisionBatchControlMapper;
    private final ProvisionCalculationMapper provisionCalculationMapper;
    private final ProvisionEclCashflowService provisionEclCashflowService;

    public ProvisionBatchServiceImpl(
            ProvisionBatchControlMapper provisionBatchControlMapper,
            ProvisionCalculationMapper provisionCalculationMapper,
            ProvisionEclCashflowService provisionEclCashflowService
    ) {
        this.provisionBatchControlMapper = provisionBatchControlMapper;
        this.provisionCalculationMapper = provisionCalculationMapper;
        this.provisionEclCashflowService = provisionEclCashflowService;
    }

    @Override
    @Transactional
    public ProvisionBatchRunResponse runBatch(ProvisionBatchRunRequest request) {
        String batchRunNo = "PRV-" + LocalDateTime.now().format(BATCH_NO_FORMAT);
        String batchType = request.getBatchType() == null || request.getBatchType().isBlank() ? "MANUAL" : request.getBatchType();

        ProvisionBatchExecution execution = ProvisionBatchExecution.start(batchRunNo, request.getBaseDate(), batchType);
        provisionBatchControlMapper.insertBatchExecution(execution);

        try {
            runStep("CLEAR_BATCH_DATA", execution.getBatchExecutionId(), () -> {
                provisionBatchControlMapper.clearBatchData(execution.getBatchExecutionId());
                return 0L;
            });

            long targetCount = runStep("TARGET_CONTRACT", execution.getBatchExecutionId(), () -> {
                provisionCalculationMapper.insertTargetContracts(execution.getBatchExecutionId(), request.getBaseDate());
                return provisionCalculationMapper.countTargetContracts(execution.getBatchExecutionId());
            });
            if (targetCount == 0L) {
                throw new BusinessException(ErrorCode.PROVISION_TARGET_NOT_FOUND);
            }

            runStep("STAGE", execution.getBatchExecutionId(), () -> {
                provisionCalculationMapper.calculateStage(execution.getBatchExecutionId());
                return provisionCalculationMapper.countStageResults(execution.getBatchExecutionId());
            });

            runStep("EAD", execution.getBatchExecutionId(), () -> {
                provisionCalculationMapper.calculateEad(execution.getBatchExecutionId());
                return provisionCalculationMapper.countEadResults(execution.getBatchExecutionId());
            });

            runStep("PD", execution.getBatchExecutionId(), () -> {
                provisionCalculationMapper.calculatePd(execution.getBatchExecutionId());
                return provisionCalculationMapper.countPdResults(execution.getBatchExecutionId());
            });

            runStep("LGD", execution.getBatchExecutionId(), () -> {
                provisionCalculationMapper.calculateLgd(execution.getBatchExecutionId());
                return provisionCalculationMapper.countLgdResults(execution.getBatchExecutionId());
            });

            runStep("ECL_CASHFLOW", execution.getBatchExecutionId(), () -> {
                return provisionEclCashflowService.buildEclCashflow(execution.getBatchExecutionId());
            });

            long processedCount = runStep("ECL", execution.getBatchExecutionId(), () -> {
                provisionCalculationMapper.calculateEcl(execution.getBatchExecutionId());
                return provisionCalculationMapper.countEclResults(execution.getBatchExecutionId());
            });

            runStep("RESULT_SUMMARY", execution.getBatchExecutionId(), () -> {
                provisionCalculationMapper.insertResultSummary(execution.getBatchExecutionId());
                return provisionCalculationMapper.countResultSummary(execution.getBatchExecutionId());
            });

            execution.complete(targetCount, processedCount, 0L);
            provisionBatchControlMapper.completeBatchExecution(execution);
        } catch (Exception exception) {
            execution.fail(limitMessage(exception.getMessage()));
            provisionBatchControlMapper.failBatchExecution(execution);
            throw exception;
        }

        return new ProvisionBatchRunResponse(
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
    public List<ProvisionBatchHistoryItemResponse> getBatchHistory(LocalDate baseDate) {
        List<ProvisionBatchHistoryItemResponse> responses = new ArrayList<>();
        for (ProvisionBatchExecution execution : provisionBatchControlMapper.findBatchHistoryByBaseDate(baseDate)) {
            responses.add(new ProvisionBatchHistoryItemResponse(
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
    public List<ProvisionBatchStepItemResponse> getStepHistory(String batchRunNo) {
        List<ProvisionBatchStepItemResponse> responses = new ArrayList<>();
        for (ProvisionBatchStepExecution step : provisionBatchControlMapper.findStepHistoryByBatchRunNo(batchRunNo)) {
            responses.add(new ProvisionBatchStepItemResponse(
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

    private long runStep(String stepName, Long batchExecutionId, StepWork stepWork) {
        ProvisionBatchStepExecution step = ProvisionBatchStepExecution.start(batchExecutionId, stepName);
        provisionBatchControlMapper.insertStepExecution(step);
        try {
            long processedCount = stepWork.run();
            step.complete(processedCount);
            provisionBatchControlMapper.completeStepExecution(step);
            return processedCount;
        } catch (Exception exception) {
            step.fail(step.getProcessedCount() == null ? 0L : step.getProcessedCount(), limitMessage(exception.getMessage()));
            provisionBatchControlMapper.failStepExecution(step);
            throw exception;
        }
    }

    private String limitMessage(String message) {
        if (message == null) {
            return null;
        }
        return message.length() > 900 ? message.substring(0, 900) : message;
    }

    @FunctionalInterface
    private interface StepWork {
        long run();
    }
}
