package com.hi.locf.feature.provision.service;

import java.time.LocalDate;
import java.util.List;

import com.hi.locf.feature.provision.dto.ProvisionBatchHistoryItemResponse;
import com.hi.locf.feature.provision.dto.ProvisionBatchRunRequest;
import com.hi.locf.feature.provision.dto.ProvisionBatchRunResponse;
import com.hi.locf.feature.provision.dto.ProvisionBatchStepItemResponse;

public interface ProvisionBatchService {

    ProvisionBatchRunResponse runBatch(ProvisionBatchRunRequest request);

    List<ProvisionBatchHistoryItemResponse> getBatchHistory(LocalDate baseDate);

    List<ProvisionBatchStepItemResponse> getStepHistory(String batchRunNo);
}
