package com.hi.locf.feature.closing.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hi.locf.common.api.ApiResponse;
import com.hi.locf.feature.closing.dto.MonthlyClosingStatusResponse;
import com.hi.locf.feature.closing.service.MonthlyClosingService;

@RestController
@RequestMapping("/api/v1/closing")
public class MonthlyClosingController {

    private final MonthlyClosingService monthlyClosingService;

    public MonthlyClosingController(MonthlyClosingService monthlyClosingService) {
        this.monthlyClosingService = monthlyClosingService;
    }

    @GetMapping("/monthly-status")
    public ApiResponse<MonthlyClosingStatusResponse> getMonthlyStatus(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate baseDate
    ) {
        return ApiResponse.ok(monthlyClosingService.getMonthlyStatus(baseDate));
    }
}
