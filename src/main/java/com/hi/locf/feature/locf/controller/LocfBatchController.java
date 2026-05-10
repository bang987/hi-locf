package com.hi.locf.feature.locf.controller;

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
import com.hi.locf.feature.locf.dto.LocfBatchHistoryItemResponse;
import com.hi.locf.feature.locf.dto.LocfBatchRunRequest;
import com.hi.locf.feature.locf.dto.LocfBatchRunResponse;
import com.hi.locf.feature.locf.dto.LocfBatchStepItemResponse;
import com.hi.locf.feature.locf.service.LocfBatchService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/locf/batches")
public class LocfBatchController {

    private final LocfBatchService locfBatchService;

    public LocfBatchController(LocfBatchService locfBatchService) {
        this.locfBatchService = locfBatchService;
    }

    @PostMapping("/run")
    public ApiResponse<LocfBatchRunResponse> runBatch(@Valid @RequestBody LocfBatchRunRequest request) {
        // 화면에서 배치 실행 버튼을 누르면 여기로 들어온다.
        return ApiResponse.ok(locfBatchService.runBatch(request));
    }

    @GetMapping
    public ApiResponse<List<LocfBatchHistoryItemResponse>> getBatchHistory(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate baseDate
    ) {
        // 기준일자를 주면 해당 일자의 배치만, 없으면 전체 배치 이력을 반환한다.
        return ApiResponse.ok(locfBatchService.getBatchHistory(baseDate));
    }

    @GetMapping("/{batchRunNo}/steps")
    public ApiResponse<List<LocfBatchStepItemResponse>> getStepHistory(@PathVariable String batchRunNo) {
        // 배치 1건 내부의 step 처리 흐름을 확인할 때 사용한다.
        return ApiResponse.ok(locfBatchService.getStepHistory(batchRunNo));
    }
}
