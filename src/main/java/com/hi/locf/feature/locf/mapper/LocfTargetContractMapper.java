package com.hi.locf.feature.locf.mapper;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.hi.locf.feature.locf.entity.LocfTargetContract;

@Mapper
public interface LocfTargetContractMapper {

    int deleteTargetContractsByBatchExecutionId(@Param("batchExecutionId") Long batchExecutionId);

    void insertTargetContracts(@Param("batchExecutionId") Long batchExecutionId, @Param("baseDate") LocalDate baseDate);

    List<LocfTargetContract> findTargetContractsByBatchExecutionId(@Param("batchExecutionId") Long batchExecutionId);

    long countTargetContracts(@Param("batchExecutionId") Long batchExecutionId);
}
