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
import com.hi.locf.feature.provision.dto.ProvisionIndividualEvalTargetRequest;
import com.hi.locf.feature.provision.dto.ProvisionIndividualEvalTargetResponse;
import com.hi.locf.feature.provision.dto.ProvisionSummaryItemResponse;

@SpringBootTest
@Transactional
class ProvisionBatchServiceIntegrationTest {

    @Autowired
    private ProvisionBatchService provisionBatchService;

    @Autowired
    private ProvisionQueryService provisionQueryService;

    @Autowired
    private ProvisionIndividualEvalTargetService provisionIndividualEvalTargetService;

    @Test
    void runProvisionBatchAndQueryResult() {
        ProvisionBatchRunRequest request = new ProvisionBatchRunRequest();
        request.setBaseDate(LocalDate.of(2026, 1, 10));
        request.setBatchType("MANUAL");

        ProvisionBatchRunResponse response = provisionBatchService.runBatch(request);

        assertThat(response.getBatchRunNo()).startsWith("PRV-");
        assertThat(response.getStatusCode()).isEqualTo("COMPLETED");
        assertThat(response.getTargetCount()).isGreaterThan(0L);
        assertThat(response.getProcessedCount()).isGreaterThan(0L);

        List<ProvisionContractResultRow> contractResults = provisionQueryService.getContractResult("LN-2026-000001");
        assertThat(contractResults).isNotEmpty();
        assertThat(contractResults.get(0).getStageCode()).isNotBlank();
        assertThat(contractResults.get(0).getCarryingAmount()).isEqualByComparingTo(new BigDecimal("9870000.00"));
        assertThat(contractResults.get(0).getEadAmount()).isNotNull();
        assertThat(contractResults.get(0).getEclAmount()).isNotNull();

        List<ProvisionSummaryItemResponse> summaries = provisionQueryService.getSummary(LocalDate.of(2026, 1, 10));
        assertThat(summaries).isNotEmpty();
        assertThat(summaries.get(0).getTotalEclAmount()).isNotNull();
    }

    @Test
    void createGetAndDeleteIndividualEvalTarget() {
        ProvisionIndividualEvalTargetRequest request = new ProvisionIndividualEvalTargetRequest();
        request.setBaseDate(LocalDate.of(2026, 1, 10));
        request.setContractNo("LN-2026-000001");
        request.setEvalReasonCode("DEFAULT");
        request.setEvalReasonDetail("통합테스트 개별평가 대상");
        request.setRecoveryExpectedAmt(new BigDecimal("5000000.00"));
        request.setDiscountRate(new BigDecimal("0.0450"));
        request.setActiveYn("Y");

        ProvisionIndividualEvalTargetResponse created = provisionIndividualEvalTargetService.createTarget(request);

        assertThat(created.getIndividualTargetId()).isNotNull();
        assertThat(created.getContractNo()).isEqualTo("LN-2026-000001");
        assertThat(created.getCustomerName()).isNotBlank();
        assertThat(created.getProductCode()).isNotBlank();

        List<ProvisionIndividualEvalTargetResponse> targets =
                provisionIndividualEvalTargetService.getTargets(LocalDate.of(2026, 1, 10), "LN-2026-000001");

        assertThat(targets).hasSize(1);
        assertThat(targets.get(0).getEvalReasonCode()).isEqualTo("DEFAULT");

        provisionIndividualEvalTargetService.deleteTarget(created.getIndividualTargetId());

        List<ProvisionIndividualEvalTargetResponse> deletedTargets =
                provisionIndividualEvalTargetService.getTargets(LocalDate.of(2026, 1, 10), "LN-2026-000001");

        assertThat(deletedTargets).isEmpty();
    }
}
