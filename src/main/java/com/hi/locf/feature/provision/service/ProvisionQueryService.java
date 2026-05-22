package com.hi.locf.feature.provision.service;

import java.time.LocalDate;
import java.util.List;

import com.hi.locf.feature.provision.dto.ProvisionContractResultRow;
import com.hi.locf.feature.provision.dto.ProvisionEclCashflowDetailResponse;
import com.hi.locf.feature.provision.dto.ProvisionSegmentSummaryItemResponse;
import com.hi.locf.feature.provision.dto.ProvisionSummaryItemResponse;

public interface ProvisionQueryService {

    List<ProvisionContractResultRow> getContractResult(String contractNo);

    List<ProvisionSummaryItemResponse> getSummary(LocalDate baseDate);

    List<ProvisionSegmentSummaryItemResponse> getSegmentSummary(LocalDate baseDate);
    
    List<ProvisionEclCashflowDetailResponse> getCashflows(String contractNo);
}
