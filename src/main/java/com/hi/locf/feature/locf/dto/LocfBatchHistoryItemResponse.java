package com.hi.locf.feature.locf.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record LocfBatchHistoryItemResponse(
        String batchRunNo,
        LocalDate baseDate,
        String batchType,
        String statusCode,
        long targetCount,
        long processedCount,
        long errorCount,
        LocalDateTime startedAt,
        LocalDateTime finishedAt,
        String errorMessage
) {
}
