package com.hi.locf.feature.locf.dto;

import java.util.List;

public record LocfContractResultResponse(
        LocfContractResultHeaderResponse header,
        List<LocfContractResultDetailRow> details
) {
}
