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
import com.hi.locf.feature.locf.entity.CustomerRatingSource;
import com.hi.locf.feature.locf.entity.LoanBalanceSource;
import com.hi.locf.feature.locf.entity.LoanCollateralSource;
import com.hi.locf.feature.locf.entity.LoanContractSource;
import com.hi.locf.feature.locf.entity.LoanDelinquencySource;
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
            createSampleContractData(i);
        }
    }

    private void createSampleContractData(int sequence) {
        CustomerMaster customer = createCustomer(sequence);
        locfSourceDataMapper.insertCustomer(customer);

        LocalDate executionDate = LocalDate.of(2026, 1, 10).plusMonths(sequence - 1L);
        long termInMonths = 12L + (sequence % 3L) * 12L;
        LocalDate maturityDate = executionDate.plusMonths(termInMonths);
        BigDecimal principal = BigDecimal.valueOf(10_000_000L + (sequence - 1L) * 1_000_000L);
        BigDecimal fee = BigDecimal.valueOf(150_000L + (sequence - 1L) * 10_000L);
        BigDecimal cost = BigDecimal.valueOf(20_000L + (sequence - 1L) * 2_000L);
        BigDecimal annualRate = BigDecimal.valueOf(12.5d + (sequence - 1) * 0.5d);

        LoanContractSource contract = createContract(
                sequence,
                customer,
                executionDate,
                maturityDate,
                principal,
                fee,
                cost
        );
        locfSourceDataMapper.insertContract(contract);

        insertRate(contract, annualRate, executionDate, maturityDate);
        insertRepaymentSchedules(contract, annualRate, termInMonths);
        insertOpeningBalance(contract, executionDate, principal);
        insertCustomerRating(customer, executionDate, sequence);
        insertDelinquency(contract, executionDate, sequence);
        insertCollateral(contract, executionDate, principal, sequence);
    }

    private CustomerMaster createCustomer(int sequence) {
        return CustomerMaster.create(
                "CUST-" + String.format("%04d", sequence),
                "고객" + sequence,
                sequence % 2 == 0 ? "CORP" : "PERSON",
                sequence % 2 == 0 ? "SME" : "RETAIL"
        );
    }

    private LoanContractSource createContract(
            int sequence,
            CustomerMaster customer,
            LocalDate executionDate,
            LocalDate maturityDate,
            BigDecimal principal,
            BigDecimal fee,
            BigDecimal cost
    ) {
        return LoanContractSource.create(
                "LN-2026-" + String.format("%06d", sequence),
                customer.getCustomerId(),
                sequence % 2 == 0 ? "MORTGAGE" : "CREDIT",
                "NORMAL",
                sequence % 3 == 0 ? "MATURITY" : "EQUAL_PAYMENT",
                executionDate,
                maturityDate,
                principal,
                fee,
                cost
        );
    }

    private void insertRate(
            LoanContractSource contract,
            BigDecimal annualRate,
            LocalDate executionDate,
            LocalDate maturityDate
    ) {
        locfSourceDataMapper.insertRate(LoanRateSource.create(
                contract.getContractId(),
                annualRate,
                executionDate,
                maturityDate
        ));
    }

    private void insertRepaymentSchedules(LoanContractSource contract, BigDecimal annualRate, long termInMonths) {
        List<LoanRepaymentScheduleSource> schedules = createSchedules(contract, annualRate, termInMonths);
        for (LoanRepaymentScheduleSource schedule : schedules) {
            locfSourceDataMapper.insertRepaymentSchedule(schedule);
        }
    }

    private void insertOpeningBalance(LoanContractSource contract, LocalDate executionDate, BigDecimal principal) {
        locfSourceDataMapper.insertBalance(LoanBalanceSource.create(
                contract.getContractId(),
                executionDate,
                principal,
                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)
        ));
    }

    private void insertCustomerRating(CustomerMaster customer, LocalDate baseDate, int sequence) {
        String[] grades = { "A", "B", "C", "D" };
        BigDecimal[] pdRates = {
                new BigDecimal("0.0100"),
                new BigDecimal("0.0300"),
                new BigDecimal("0.0700"),
                new BigDecimal("0.1800")
        };
        int index = (sequence - 1) % grades.length;
        locfSourceDataMapper.insertCustomerRating(
                CustomerRatingSource.create(customer.getCustomerId(), baseDate, grades[index], pdRates[index])
        );
    }

    private void insertDelinquency(LoanContractSource contract, LocalDate baseDate, int sequence) {
        long delinquencyDays;
        String defaultYn;

        if (sequence % 5 == 0) {
            delinquencyDays = 120L;
            defaultYn = "Y";
        } else if (sequence % 4 == 0) {
            delinquencyDays = 45L;
            defaultYn = "N";
        } else if (sequence % 3 == 0) {
            delinquencyDays = 10L;
            defaultYn = "N";
        } else {
            delinquencyDays = 0L;
            defaultYn = "N";
        }

        locfSourceDataMapper.insertDelinquency(
                LoanDelinquencySource.create(contract.getContractId(), baseDate, delinquencyDays, defaultYn)
        );
    }

    private void insertCollateral(LoanContractSource contract, LocalDate baseDate, BigDecimal principal, int sequence) {
        String collateralType = sequence % 2 == 0 ? "REAL_ESTATE" : "UNSECURED";
        BigDecimal recoveryRate = sequence % 2 == 0 ? new BigDecimal("0.75") : new BigDecimal("0.25");
        BigDecimal collateralValue = principal.multiply(sequence % 2 == 0 ? new BigDecimal("0.85") : new BigDecimal("0.20"), MC)
                .setScale(2, RoundingMode.HALF_UP);

        locfSourceDataMapper.insertCollateral(
                LoanCollateralSource.create(contract.getContractId(), baseDate, collateralType, collateralValue, recoveryRate)
        );
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
