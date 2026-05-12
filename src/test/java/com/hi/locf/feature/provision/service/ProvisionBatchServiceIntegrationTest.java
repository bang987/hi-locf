package com.hi.locf.feature.provision.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.hi.locf.feature.provision.dto.ProvisionBatchRunRequest;
import com.hi.locf.feature.provision.dto.ProvisionBatchRunResponse;
import com.hi.locf.feature.provision.dto.ProvisionContractResultRow;
import com.hi.locf.feature.provision.dto.ProvisionSummaryItemResponse;

@SpringBootTest
@Transactional
class ProvisionBatchServiceIntegrationTest {

    @Autowired
    private ProvisionBatchService provisionBatchService;

    @Autowired
    private ProvisionQueryService provisionQueryService;

    @Test
    void runProvisionBatchAndQueryResult() {
        ProvisionBatchRunRequest request = new ProvisionBatchRunRequest();
        request.setBaseDate(LocalDate.of(2026, 1, 10));
        request.setBatchType("MANUAL");

        ProvisionBatchRunResponse response = provisionBatchService.runBatch(request);

        assertThat(response.batchRunNo()).startsWith("PRV-");
        assertThat(response.statusCode()).isEqualTo("COMPLETED");
        assertThat(response.targetCount()).isGreaterThan(0L);
        assertThat(response.processedCount()).isGreaterThan(0L);

        List<ProvisionContractResultRow> contractResults = provisionQueryService.getContractResult("LN-2026-000001");
        assertThat(contractResults).isNotEmpty();
        assertThat(contractResults.get(0).stageCode()).isNotBlank();
        assertThat(contractResults.get(0).carryingAmount()).isEqualByComparingTo(new BigDecimal("9870000.00"));
        assertThat(contractResults.get(0).eadAmount()).isNotNull();
        assertThat(contractResults.get(0).eclAmount()).isNotNull();

        List<ProvisionSummaryItemResponse> summaries = provisionQueryService.getSummary(LocalDate.of(2026, 1, 10));
        assertThat(summaries).isNotEmpty();
        assertThat(summaries.get(0).totalEclAmount()).isNotNull();
    }
}
