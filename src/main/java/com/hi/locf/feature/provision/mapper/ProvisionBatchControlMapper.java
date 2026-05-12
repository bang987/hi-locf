package com.hi.locf.feature.provision.mapper;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.hi.locf.feature.provision.entity.ProvisionBatchExecution;
import com.hi.locf.feature.provision.entity.ProvisionBatchStepExecution;

@Mapper
public interface ProvisionBatchControlMapper {

    int insertBatchExecution(ProvisionBatchExecution execution);

    int completeBatchExecution(ProvisionBatchExecution execution);

    int failBatchExecution(ProvisionBatchExecution execution);

    List<ProvisionBatchExecution> findBatchHistoryByBaseDate(@Param("baseDate") LocalDate baseDate);

    int insertStepExecution(ProvisionBatchStepExecution execution);

    int completeStepExecution(ProvisionBatchStepExecution execution);

    int failStepExecution(ProvisionBatchStepExecution execution);

    List<ProvisionBatchStepExecution> findStepHistoryByBatchRunNo(@Param("batchRunNo") String batchRunNo);

    void clearBatchData(@Param("batchExecutionId") Long batchExecutionId);
}
