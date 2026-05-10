package com.hi.locf.feature.locf.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.hi.locf.feature.locf.entity.LocfEirResult;

@Mapper
public interface LocfEirMapper {

    int deleteEirResultsByBatchExecutionId(@Param("batchExecutionId") Long batchExecutionId);

    int insertEirResult(LocfEirResult result);

    LocfEirResult findEirResultByContractId(@Param("batchExecutionId") Long batchExecutionId, @Param("contractId") Long contractId);

    List<LocfEirResult> findEirResultsByBatchExecutionId(@Param("batchExecutionId") Long batchExecutionId);
}
