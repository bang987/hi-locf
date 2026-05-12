package com.hi.locf.feature.provision.dto;

import java.time.LocalDateTime;

public record ProvisionBatchStepItemResponse(
        String stepName,
        String statusCode,
        long processedCount,
        LocalDateTime startedAt,
        LocalDateTime finishedAt,
        String errorMessage
) {
}
