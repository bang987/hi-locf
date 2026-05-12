package com.hi.locf.feature.provision.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ProvisionBatchHistoryItemResponse(
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
