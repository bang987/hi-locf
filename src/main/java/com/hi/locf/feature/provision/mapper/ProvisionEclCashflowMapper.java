package com.hi.locf.feature.provision.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.hi.locf.feature.provision.entity.ProvisionEclCashflowDetail;
import com.hi.locf.feature.provision.entity.ProvisionEclCashflowSourceRow;

@Mapper
public interface ProvisionEclCashflowMapper {

    List<ProvisionEclCashflowSourceRow> findCashflowSourceRows(@Param("batchExecutionId") Long batchExecutionId);

    int insertEclCashflowDetail(ProvisionEclCashflowDetail detail);

    long countEclCashflowResults(@Param("batchExecutionId") Long batchExecutionId);
}
