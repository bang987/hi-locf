package com.hi.locf.feature.closing.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hi.locf.feature.closing.dto.MonthlyClosingStatusResponse;
import com.hi.locf.feature.closing.mapper.MonthlyClosingMapper;

@Service
public class MonthlyClosingServiceImpl implements MonthlyClosingService {

    private final MonthlyClosingMapper monthlyClosingMapper;

    public MonthlyClosingServiceImpl(MonthlyClosingMapper monthlyClosingMapper) {
        this.monthlyClosingMapper = monthlyClosingMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public MonthlyClosingStatusResponse getMonthlyStatus(LocalDate baseDate) {
        return monthlyClosingMapper.findMonthlyStatus(baseDate);
    }
}
