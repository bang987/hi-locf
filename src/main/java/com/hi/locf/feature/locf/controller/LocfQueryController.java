package com.hi.locf.feature.locf.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hi.locf.common.api.ApiResponse;
import com.hi.locf.feature.locf.dto.LocfContractResultResponse;
import com.hi.locf.feature.locf.dto.LocfSourceContractItemResponse;
import com.hi.locf.feature.locf.dto.LocfSummaryItemResponse;
import com.hi.locf.feature.locf.service.LocfQueryService;

@RestController
@RequestMapping("/api/v1/locf")
public class LocfQueryController {

    private final LocfQueryService locfQueryService;

    public LocfQueryController(LocfQueryService locfQueryService) {
        this.locfQueryService = locfQueryService;
    }

    @GetMapping("/contracts/{contractNo}")
    public ApiResponse<LocfContractResultResponse> getContractResult(@PathVariable String contractNo) {
        // 계약번호로 최신 LOCF 산출 결과를 조회한다.
    	//@RestController가 붙어 있으면 Spring은
    	//Controller가 반환한 객체를 HTTP Response Body로 써야 한다고 판단합니다.
    	//그래서 내부적으로: Jackson 같은 JSON 변환기 getter 기준으로 객체를 JSON 문자열로 바꿉니다.
        return ApiResponse.ok(locfQueryService.getContractResult(contractNo));
    }

    @GetMapping("/source-contracts")
    public ApiResponse<List<LocfSourceContractItemResponse>> getSourceContracts() {
        // 원천 계약/금리/잔액 상태를 화면에서 먼저 점검할 때 사용한다.
        return ApiResponse.ok(locfQueryService.getSourceContracts());
    }

    @GetMapping("/results/summary")
    public ApiResponse<List<LocfSummaryItemResponse>> getSummary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate baseDate
    ) {
        // 기준일자 단위 요약 결과를 상품코드별로 반환한다.
        return ApiResponse.ok(locfQueryService.getSummary(baseDate));
    }
}
