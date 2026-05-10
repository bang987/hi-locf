package com.hi.locf.feature.locf.service;

import java.time.LocalDate;
import java.util.List;

import com.hi.locf.feature.locf.dto.LocfContractResultResponse;
import com.hi.locf.feature.locf.dto.LocfSourceContractItemResponse;
import com.hi.locf.feature.locf.dto.LocfSummaryItemResponse;

public interface LocfQueryService {

    LocfContractResultResponse getContractResult(String contractNo);

    List<LocfSummaryItemResponse> getSummary(LocalDate baseDate);

    List<LocfSourceContractItemResponse> getSourceContracts();
}
