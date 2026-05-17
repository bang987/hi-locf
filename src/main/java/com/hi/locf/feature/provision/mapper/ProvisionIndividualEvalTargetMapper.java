package com.hi.locf.feature.provision.mapper;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.hi.locf.feature.provision.entity.ProvisionIndividualEvalTarget;

@Mapper
public interface ProvisionIndividualEvalTargetMapper {

    int insertTarget(ProvisionIndividualEvalTarget target);

    List<ProvisionIndividualEvalTarget> findTargets(@Param("baseDate") LocalDate baseDate,
                                                    @Param("contractNo") String contractNo);

    ProvisionIndividualEvalTarget findTargetById(@Param("individualTargetId") Long individualTargetId);

    ProvisionIndividualEvalTarget findDuplicateTarget(@Param("baseDate") LocalDate baseDate,
                                                      @Param("contractId") Long contractId);

    int deleteTargetById(@Param("individualTargetId") Long individualTargetId);
}
