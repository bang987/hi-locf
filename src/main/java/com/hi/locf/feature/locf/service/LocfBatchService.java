package com.hi.locf.feature.locf.service;

import java.time.LocalDate;
import java.util.List;

import com.hi.locf.feature.locf.dto.LocfBatchHistoryItemResponse;
import com.hi.locf.feature.locf.dto.LocfBatchRunRequest;
import com.hi.locf.feature.locf.dto.LocfBatchRunResponse;
import com.hi.locf.feature.locf.dto.LocfBatchStepItemResponse;


public interface LocfBatchService {

    LocfBatchRunResponse runBatch(LocfBatchRunRequest request);

    List<LocfBatchHistoryItemResponse> getBatchHistory(LocalDate baseDate);

    List<LocfBatchStepItemResponse> getStepHistory(String batchRunNo);
    
}
