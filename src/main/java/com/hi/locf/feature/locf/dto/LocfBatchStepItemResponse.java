package com.hi.locf.feature.locf.dto;

import java.time.LocalDateTime;

public record LocfBatchStepItemResponse(
        String stepName,
        String statusCode,
        long processedCount,
        LocalDateTime startedAt,
        LocalDateTime finishedAt,
        String errorMessage
) {
}
