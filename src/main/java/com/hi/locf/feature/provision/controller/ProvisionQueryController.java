package com.hi.locf.feature.provision.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hi.locf.common.api.ApiResponse;
import com.hi.locf.feature.provision.dto.ProvisionContractResultRow;
import com.hi.locf.feature.provision.dto.ProvisionSummaryItemResponse;
import com.hi.locf.feature.provision.service.ProvisionQueryService;

@RestController
@RequestMapping("/api/v1/provision")
public class ProvisionQueryController {

    private final ProvisionQueryService provisionQueryService;

    public ProvisionQueryController(ProvisionQueryService provisionQueryService) {
        this.provisionQueryService = provisionQueryService;
    }

    @GetMapping("/contracts/{contractNo}")
    public ApiResponse<List<ProvisionContractResultRow>> getContractResult(@PathVariable String contractNo) {
        return ApiResponse.ok(provisionQueryService.getContractResult(contractNo));
    }

    @GetMapping("/results/summary")
    public ApiResponse<List<ProvisionSummaryItemResponse>> getSummary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate baseDate
    ) {
        return ApiResponse.ok(provisionQueryService.getSummary(baseDate));
    }
}
