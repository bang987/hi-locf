package com.hi.locf.feature.provision.mapper;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.hi.locf.feature.provision.entity.ProvisionContractResultDetail;
import com.hi.locf.feature.provision.entity.ProvisionResultSummary;

@Mapper
public interface ProvisionQueryMapper {

    List<ProvisionContractResultDetail> findLatestContractResultsByContractNo(@Param("contractNo") String contractNo);

    List<ProvisionResultSummary> findSummaryByBaseDate(@Param("baseDate") LocalDate baseDate);
}
