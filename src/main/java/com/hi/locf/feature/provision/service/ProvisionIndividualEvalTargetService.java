package com.hi.locf.feature.provision.service;

import java.time.LocalDate;
import java.util.List;

import com.hi.locf.feature.provision.dto.ProvisionIndividualEvalTargetRequest;
import com.hi.locf.feature.provision.dto.ProvisionIndividualEvalTargetResponse;

public interface ProvisionIndividualEvalTargetService {
	
    ProvisionIndividualEvalTargetResponse createTarget(ProvisionIndividualEvalTargetRequest request); // 등록

    List<ProvisionIndividualEvalTargetResponse> getTargets(LocalDate baseDate, String contractNo); //조회 

    void deleteTarget(Long individualTargetId); //삭제

}
