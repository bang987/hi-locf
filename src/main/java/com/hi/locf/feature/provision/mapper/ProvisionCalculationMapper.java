package com.hi.locf.feature.provision.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProvisionCalculationMapper {

    void insertTargetContracts(@Param("batchExecutionId") Long batchExecutionId, @Param("baseDate") java.time.LocalDate baseDate);

    long countTargetContracts(@Param("batchExecutionId") Long batchExecutionId);

    void calculateStage(@Param("batchExecutionId") Long batchExecutionId);

    long countStageResults(@Param("batchExecutionId") Long batchExecutionId);

    void calculateEad(@Param("batchExecutionId") Long batchExecutionId);

    long countEadResults(@Param("batchExecutionId") Long batchExecutionId);

    void calculatePd(@Param("batchExecutionId") Long batchExecutionId);

    long countPdResults(@Param("batchExecutionId") Long batchExecutionId);

    void calculateLgd(@Param("batchExecutionId") Long batchExecutionId);

    long countLgdResults(@Param("batchExecutionId") Long batchExecutionId);

    void buildEclCashflow(@Param("batchExecutionId") Long batchExecutionId);

    long countEclCashflowResults(@Param("batchExecutionId") Long batchExecutionId);

    void calculateEcl(@Param("batchExecutionId") Long batchExecutionId);

    long countEclResults(@Param("batchExecutionId") Long batchExecutionId);

    void insertResultSummary(@Param("batchExecutionId") Long batchExecutionId);

    long countResultSummary(@Param("batchExecutionId") Long batchExecutionId);
}
