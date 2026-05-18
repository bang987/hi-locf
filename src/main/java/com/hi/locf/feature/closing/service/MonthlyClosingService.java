package com.hi.locf.feature.closing.service;

import java.time.LocalDate;

import com.hi.locf.feature.closing.dto.MonthlyClosingStatusResponse;

public interface MonthlyClosingService {

    MonthlyClosingStatusResponse getMonthlyStatus(LocalDate baseDate);
}
