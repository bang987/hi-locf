package com.hi.locf.feature.locf.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record LocfBatchRunResponse(
        String batchRunNo,
        LocalDate baseDate,
        String statusCode,
        long targetCount,
        long processedCount,
        long errorCount,
        LocalDateTime startedAt,
        LocalDateTime finishedAt
) {
}
