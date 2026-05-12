package com.hi.locf.feature.provision.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ProvisionBatchRunResponse(
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
