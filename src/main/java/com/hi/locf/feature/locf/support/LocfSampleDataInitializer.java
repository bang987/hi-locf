package com.hi.locf.feature.locf.support;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hi.locf.config.BatchSeedProperties;
import com.hi.locf.feature.locf.entity.CustomerMaster;
import com.hi.locf.feature.locf.entity.LoanBalanceSource;
import com.hi.locf.feature.locf.entity.LoanContractSource;
import com.hi.locf.feature.locf.entity.LoanRateSource;
import com.hi.locf.feature.locf.entity.LoanRepaymentScheduleSource;
import com.hi.locf.feature.locf.mapper.LocfSourceDataMapper;

import jakarta.annotation.PostConstruct;

@Component
public class LocfSampleDataInitializer {

    private static final MathContext MC = new MathContext(20, RoundingMode.HALF_UP);

    private final LocfSourceDataMapper locfSourceDataMapper;
    private final BatchSeedProperties batchSeedProperties;

    public LocfSampleDataInitializer(LocfSourceDataMapper locfSourceDataMapper, BatchSeedProperties batchSeedProperties) {
        this.locfSourceDataMapper = locfSourceDataMapper;
        this.batchSeedProperties = batchSeedProperties;
    }

    @PostConstruct
    public void initialize() {
        if (locfSourceDataMapper.countContracts() > 0) {
            return;
        }

        int count = Math.max(3, batchSeedProperties.getSeedContractCount());
        for (int i = 1; i <= count; i++) {
            CustomerMaster customer = CustomerMaster.create(
                    "CUST-" + String.format("%04d", i),
                    "고객" + i,
                    i % 2 == 0 ? "CORP" : "PERSON",
                    i % 2 == 0 ? "SME" : "RETAIL"
            );
            locfSourceDataMapper.insertCustomer(customer);

            LocalDate executionDate = LocalDate.of(2026, 1, 10).plusMonths(i - 1L);
            long term = 12L + (i % 3L) * 12L;
            LocalDate maturityDate = executionDate.plusMonths(term);
            BigDecimal principal = BigDecimal.valueOf(10_000_000L + (i - 1L) * 1_000_000L);
            BigDecimal fee = BigDecimal.valueOf(150_000L + (i - 1L) * 10_000L);
            BigDecimal cost = BigDecimal.valueOf(20_000L + (i - 1L) * 2_000L);
            BigDecimal annualRate = BigDecimal.valueOf(12.5d + (i - 1) * 0.5d);

            LoanContractSource contract = LoanContractSource.create(
                    "LN-2026-" + String.format("%06d", i),
                    customer.getCustomerId(),
                    i % 2 == 0 ? "MORTGAGE" : "CREDIT",
                    "NORMAL",
                    i % 3 == 0 ? "MATURITY" : "EQUAL_PAYMENT",
                    executionDate,
                    maturityDate,
                    principal,
                    fee,
                    cost
            );
            locfSourceDataMapper.insertContract(contract);

            locfSourceDataMapper.insertRate(LoanRateSource.create(
                    contract.getContractId(),
                    annualRate,
                    executionDate,
                    maturityDate
            ));

            List<LoanRepaymentScheduleSource> schedules = createSchedules(contract, annualRate, term);
            for (LoanRepaymentScheduleSource schedule : schedules) {
                locfSourceDataMapper.insertRepaymentSchedule(schedule);
            }

            locfSourceDataMapper.insertBalance(LoanBalanceSource.create(
                    contract.getContractId(),
                    executionDate,
                    principal,
                    BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)
            ));
        }
    }

    private List<LoanRepaymentScheduleSource> createSchedules(LoanContractSource contract, BigDecimal annualRate, long termInMonths) {
        List<LoanRepaymentScheduleSource> rows = new ArrayList<>();
        BigDecimal monthlyRate = annualRate.divide(BigDecimal.valueOf(100L), MC)
                .divide(BigDecimal.valueOf(12L), MC);
        BigDecimal balance = contract.getPrincipalAmt();

        if ("MATURITY".equals(contract.getRepaymentType())) {
            for (long installment = 1; installment <= termInMonths; installment++) {
                BigDecimal interest = balance.multiply(monthlyRate, MC).setScale(2, RoundingMode.HALF_UP);
                BigDecimal principal = installment == termInMonths ? balance : BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
                BigDecimal payment = interest.add(principal, MC).setScale(2, RoundingMode.HALF_UP);
                rows.add(LoanRepaymentScheduleSource.create(
                        contract.getContractId(),
                        installment,
                        contract.getExecutionDate().plusMonths(installment),
                        payment,
                        principal,
                        interest
                ));
            }
            return rows;
        }

        BigDecimal installmentAmount = calculateInstallmentAmount(contract.getPrincipalAmt(), monthlyRate, termInMonths);
        for (long installment = 1; installment <= termInMonths; installment++) {
            BigDecimal interest = balance.multiply(monthlyRate, MC).setScale(2, RoundingMode.HALF_UP);
            BigDecimal principal = installmentAmount.subtract(interest, MC).setScale(2, RoundingMode.HALF_UP);
            if (installment == termInMonths) {
                principal = balance.setScale(2, RoundingMode.HALF_UP);
                installmentAmount = principal.add(interest, MC).setScale(2, RoundingMode.HALF_UP);
            }
            rows.add(LoanRepaymentScheduleSource.create(
                    contract.getContractId(),
                    installment,
                    contract.getExecutionDate().plusMonths(installment),
                    installmentAmount,
                    principal,
                    interest
            ));
            balance = balance.subtract(principal, MC);
        }
        return rows;
    }

    private BigDecimal calculateInstallmentAmount(BigDecimal principal, BigDecimal monthlyRate, long termInMonths) {
        BigDecimal onePlusRatePow = BigDecimal.ONE.add(monthlyRate, MC).pow((int) termInMonths, MC);
        BigDecimal numerator = principal.multiply(monthlyRate, MC).multiply(onePlusRatePow, MC);
        BigDecimal denominator = onePlusRatePow.subtract(BigDecimal.ONE, MC);
        return numerator.divide(denominator, 2, RoundingMode.HALF_UP);
    }
}
