package com.hi.locf.feature.locf.mapper;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.hi.locf.feature.locf.entity.LocfAmortizationDetail;
import com.hi.locf.feature.locf.entity.LoanContractSource;
import com.hi.locf.feature.locf.entity.LocfResultHeader;
import com.hi.locf.feature.locf.entity.LocfResultSummary;

@Mapper
public interface LocfQueryMapper {

    LocfResultHeader findLatestResultHeaderByContractNo(@Param("contractNo") String contractNo);

    List<LocfAmortizationDetail> findLatestResultDetailsByContractNo(@Param("contractNo") String contractNo);

    List<LocfResultSummary> findSummaryByBaseDate(@Param("baseDate") LocalDate baseDate);

    List<LoanContractSource> findSourceContracts();
}
