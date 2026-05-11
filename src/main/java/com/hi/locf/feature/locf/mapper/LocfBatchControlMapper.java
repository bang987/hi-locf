package com.hi.locf.feature.locf.mapper;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.hi.locf.feature.locf.entity.LocfBatchExecution;
import com.hi.locf.feature.locf.entity.LocfBatchStepExecution;

@Mapper
public interface LocfBatchControlMapper {

    int insertBatchExecution(LocfBatchExecution execution);

    int completeBatchExecution(LocfBatchExecution execution);

    int failBatchExecution(LocfBatchExecution execution);

    List<LocfBatchExecution> findBatchHistoryByBaseDate(@Param("baseDate") LocalDate baseDate);

    int insertStepExecution(LocfBatchStepExecution execution);

    int completeStepExecution(LocfBatchStepExecution execution);

    int failStepExecution(LocfBatchStepExecution execution);

    List<LocfBatchStepExecution> findStepHistoryByBatchRunNo(@Param("batchRunNo") String batchRunNo);

    void clearBatchData(@Param("batchExecutionId") Long batchExecutionId);
}
