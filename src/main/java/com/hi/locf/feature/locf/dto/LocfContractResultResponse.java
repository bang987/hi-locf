package com.hi.locf.feature.locf.dto;

import java.util.List;

public class LocfContractResultResponse {

    private LocfContractResultHeaderResponse header;
    private List<LocfContractResultDetailRow> details;

    public LocfContractResultResponse() {
    }

    public LocfContractResultResponse(LocfContractResultHeaderResponse header, List<LocfContractResultDetailRow> details) {
        this.header = header;
        this.details = details;
    }

    public LocfContractResultHeaderResponse getHeader() {
        return header;
    }

    public void setHeader(LocfContractResultHeaderResponse header) {
        this.header = header;
    }

    public List<LocfContractResultDetailRow> getDetails() {
        return details;
    }

    public void setDetails(List<LocfContractResultDetailRow> details) {
        this.details = details;
    }
}
