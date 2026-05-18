package com.hi.locf.feature.closing.mapper;

import java.time.LocalDate;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.hi.locf.feature.closing.dto.MonthlyClosingStatusResponse;

@Mapper
public interface MonthlyClosingMapper {

    MonthlyClosingStatusResponse findMonthlyStatus(@Param("baseDate") LocalDate baseDate);
}
