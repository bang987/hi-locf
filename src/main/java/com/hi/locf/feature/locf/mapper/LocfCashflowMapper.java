package com.hi.locf.feature.locf.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.hi.locf.feature.locf.entity.LocfCashflowBase;

@Mapper
public interface LocfCashflowMapper {

    int deleteCashflowBaseByBatchExecutionId(@Param("batchExecutionId") Long batchExecutionId);

    int insertCashflowBase(LocfCashflowBase cashflowBase);

    List<LocfCashflowBase> findCashflowBaseByBatchExecutionId(@Param("batchExecutionId") Long batchExecutionId);

    List<LocfCashflowBase> findCashflowBaseByContractId(
            @Param("batchExecutionId") Long batchExecutionId,
            @Param("contractId") Long contractId
    );
}
