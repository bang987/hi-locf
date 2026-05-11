package com.hi.locf.feature.locf.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.hi.locf.feature.locf.entity.LocfAmortizationDetail;
import com.hi.locf.feature.locf.entity.LocfResultHeader;

@Mapper
public interface LocfAmortizationMapper {

    int deleteAmortizationDetailsByBatchExecutionId(@Param("batchExecutionId") Long batchExecutionId);

    int insertAmortizationDetail(LocfAmortizationDetail detail);

    int deleteResultHeadersByBatchExecutionId(@Param("batchExecutionId") Long batchExecutionId);

    int insertResultHeader(LocfResultHeader header);

    int deleteResultSummaryByBatchExecutionId(@Param("batchExecutionId") Long batchExecutionId);

    void insertResultSummary(@Param("batchExecutionId") Long batchExecutionId);

    long countResultSummary(@Param("batchExecutionId") Long batchExecutionId);
}
