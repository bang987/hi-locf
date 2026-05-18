package com.hi.locf.feature.provision.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hi.locf.common.api.ApiResponse;
import com.hi.locf.feature.provision.dto.ProvisionIndividualEvalTargetRequest;
import com.hi.locf.feature.provision.dto.ProvisionIndividualEvalTargetResponse;
import com.hi.locf.feature.provision.service.ProvisionIndividualEvalTargetService;

@RestController
@RequestMapping("/api/v1/provision/individual-targets")
public class ProvisionIndividualEvalTargetController {

    private final ProvisionIndividualEvalTargetService provisionIndividualEvalTargetService;

    public ProvisionIndividualEvalTargetController(
            ProvisionIndividualEvalTargetService provisionIndividualEvalTargetService
    ) {
        this.provisionIndividualEvalTargetService = provisionIndividualEvalTargetService;
    }

    //저장
    @PostMapping
    public ApiResponse<ProvisionIndividualEvalTargetResponse> createTarget(
            @RequestBody ProvisionIndividualEvalTargetRequest request
    ) {
        return ApiResponse.ok(provisionIndividualEvalTargetService.createTarget(request));
    }

    //조회
    @GetMapping
    public ApiResponse<List<ProvisionIndividualEvalTargetResponse>> getTargets(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate baseDate,
            @RequestParam(required = false) String contractNo
    ) {
        return ApiResponse.ok(provisionIndividualEvalTargetService.getTargets(baseDate, contractNo));
    }

    @DeleteMapping("/{individualTargetId}")
    public ApiResponse<Void> deleteTarget(@PathVariable Long individualTargetId) {
        provisionIndividualEvalTargetService.deleteTarget(individualTargetId);
        return ApiResponse.ok(null);
    }
}
