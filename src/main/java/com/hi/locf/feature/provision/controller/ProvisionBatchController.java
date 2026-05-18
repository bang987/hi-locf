package com.hi.locf.feature.provision.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hi.locf.common.api.ApiResponse;
import com.hi.locf.feature.provision.dto.ProvisionBatchHistoryItemResponse;
import com.hi.locf.feature.provision.dto.ProvisionBatchRunRequest;
import com.hi.locf.feature.provision.dto.ProvisionBatchRunResponse;
import com.hi.locf.feature.provision.dto.ProvisionBatchStepItemResponse;
import com.hi.locf.feature.provision.service.ProvisionBatchService;

import jakarta.validation.Valid;
                
@RestController
@RequestMapping("/api/v1/provision/batches")
public class ProvisionBatchController {

    private final ProvisionBatchService provisionBatchService;

    public ProvisionBatchController(ProvisionBatchService provisionBatchService) {
        this.provisionBatchService = provisionBatchService;
    }

    @PostMapping("/run")
    public ApiResponse<ProvisionBatchRunResponse> runBatch(@Valid @RequestBody ProvisionBatchRunRequest request) {
        return ApiResponse.ok(provisionBatchService.runBatch(request));
    }

    @GetMapping
    public ApiResponse<List<ProvisionBatchHistoryItemResponse>> getBatchHistory(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate baseDate
    ) {
        return ApiResponse.ok(provisionBatchService.getBatchHistory(baseDate));
    }

    @GetMapping("/{batchRunNo}/steps")
    public ApiResponse<List<ProvisionBatchStepItemResponse>> getStepHistory(@PathVariable String batchRunNo) {
        return ApiResponse.ok(provisionBatchService.getStepHistory(batchRunNo));
    }
}
