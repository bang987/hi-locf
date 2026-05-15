package com.hi.locf.feature.locf.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hi.locf.feature.locf.dto.LocfBatchRunRequest;

@SpringBootTest
class LocfBatchServiceIntegrationTest {

    @Autowired
    private LocfBatchService locfBatchService;

    @Autowired
    private LocfQueryService locfQueryService;

    @Test
    void runBatchAndQueryResult() {
        LocfBatchRunRequest request = new LocfBatchRunRequest();
        request.setBaseDate(LocalDate.of(2026, 1, 10));
        request.setBatchType("TEST");

        var response = locfBatchService.runBatch(request);

        assertThat(response.getStatusCode()).isEqualTo("COMPLETED");
        assertThat(response.getTargetCount()).isGreaterThan(0L);
        assertThat(response.getProcessedCount()).isGreaterThan(0L);

        var contractResult = locfQueryService.getContractResult("LN-2026-000001");
        assertThat(contractResult.getHeader().getContractNo()).isEqualTo("LN-2026-000001");
        assertThat(contractResult.getDetails()).isNotEmpty();
        assertThat(contractResult.getDetails().get(0).getOpeningPrincipalBal()).isEqualByComparingTo(new BigDecimal("10000000.00"));
        assertThat(contractResult.getDetails().get(0).getOpeningCarryingAmt()).isGreaterThan(BigDecimal.ZERO);

        var summary = locfQueryService.getSummary(LocalDate.of(2026, 1, 10));
        assertThat(summary).isNotEmpty();

        var sourceContracts = locfQueryService.getSourceContracts();
        assertThat(sourceContracts).isNotEmpty();
        assertThat(sourceContracts.get(0).getAnnualNominalRate()).isNotNull();

        var stepHistory = locfBatchService.getStepHistory(response.getBatchRunNo());
        assertThat(stepHistory).isNotEmpty();
        assertThat(stepHistory).extracting("stepName")
                .contains("TARGET_CONTRACT", "CASHFLOW", "EIR", "AMORTIZATION", "RESULT_SUMMARY");
    }
}
