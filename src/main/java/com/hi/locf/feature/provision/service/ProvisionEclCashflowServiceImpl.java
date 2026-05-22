package com.hi.locf.feature.provision.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hi.locf.feature.provision.entity.ProvisionEclCashflowDetail;
import com.hi.locf.feature.provision.entity.ProvisionEclCashflowSourceRow;
import com.hi.locf.feature.provision.mapper.ProvisionEclCashflowMapper;

@Service
public class ProvisionEclCashflowServiceImpl implements ProvisionEclCashflowService {

    private static final MathContext MC = new MathContext(20, RoundingMode.HALF_UP);
    private static final BigDecimal ZERO_RATE = BigDecimal.ZERO.setScale(10, RoundingMode.HALF_UP);
    private static final BigDecimal ONE = BigDecimal.ONE.setScale(10, RoundingMode.HALF_UP);
    private static final BigDecimal TWELVE = new BigDecimal("12");

    private final ProvisionEclCashflowMapper provisionEclCashflowMapper;

    public ProvisionEclCashflowServiceImpl(ProvisionEclCashflowMapper provisionEclCashflowMapper) {
        this.provisionEclCashflowMapper = provisionEclCashflowMapper;
    }

    @Override
    public long buildEclCashflow(Long batchExecutionId) {
        List<ProvisionEclCashflowSourceRow> sourceRows =
                provisionEclCashflowMapper.findCashflowSourceRows(batchExecutionId);

        long insertedCount = 0L;
        for (List<ProvisionEclCashflowSourceRow> contractRows : groupByTarget(sourceRows)) {
            insertedCount += insertContractCashflows(contractRows);
        }
        return insertedCount;
    }

    private long insertContractCashflows(List<ProvisionEclCashflowSourceRow> contractRows) {
        int remainingCount = contractRows.size();
        long insertedCount = 0L;
        for (int index = 0; index < contractRows.size(); index++) {
            ProvisionEclCashflowSourceRow sourceRow = contractRows.get(index);
            int periodNo = index + 1;

            BigDecimal marginalPdRate = calculateMarginalPdRate(sourceRow, remainingCount);
            BigDecimal cumulativePdRate = calculateCumulativePdRate(sourceRow, remainingCount, periodNo);
            BigDecimal discountFactor = calculateDiscountFactor(
                    sourceRow.getMonthlyEir(),
                    sourceRow.getBaseDate(),
                    sourceRow.getCashflowDate()
            );

            BigDecimal periodEclAmount = sourceRow.getBeginningEadAmount()
                    .multiply(marginalPdRate, MC)
                    .multiply(sourceRow.getLgdRate(), MC)
                    .setScale(2, RoundingMode.HALF_UP);
            BigDecimal pvEclAmount = periodEclAmount
                    .multiply(discountFactor, MC)
                    .setScale(2, RoundingMode.HALF_UP);

            ProvisionEclCashflowDetail detail = new ProvisionEclCashflowDetail();
            detail.setBatchExecutionId(sourceRow.getBatchExecutionId());
            detail.setTargetId(sourceRow.getTargetId());
            detail.setContractId(sourceRow.getContractId());
            detail.setContractNo(sourceRow.getContractNo());
            detail.setInstallmentNo(sourceRow.getInstallmentNo());
            detail.setCashflowDate(sourceRow.getCashflowDate());
            detail.setBeginningEadAmount(sourceRow.getBeginningEadAmount());
            detail.setExpectedPrincipalAmt(sourceRow.getExpectedPrincipalAmt());
            detail.setExpectedInterestAmt(sourceRow.getExpectedInterestAmt());
            detail.setEndingEadAmount(sourceRow.getEndingEadAmount());
            detail.setMarginalPdRate(marginalPdRate);
            detail.setCumulativePdRate(cumulativePdRate);
            detail.setLgdRate(sourceRow.getLgdRate());
            detail.setDiscountRate(nullToZero(sourceRow.getMonthlyEir()));
            detail.setDiscountFactor(discountFactor);
            detail.setPeriodEclAmount(periodEclAmount);
            detail.setPvEclAmount(pvEclAmount);

            provisionEclCashflowMapper.insertEclCashflowDetail(detail);
            insertedCount++;
        }
        return insertedCount;
    }

    private BigDecimal calculateMarginalPdRate(ProvisionEclCashflowSourceRow sourceRow, int remainingCount) {
        if ("STAGE1".equals(sourceRow.getStageCode())) {
            if (remainingCount == 1) {
                return sourceRow.getOneYearPdRate().setScale(10, RoundingMode.HALF_UP);
            }
            if (monthsCeil(sourceRow.getBaseDate(), sourceRow.getCashflowDate()) <= 12) {
                return sourceRow.getOneYearPdRate().divide(TWELVE, 10, RoundingMode.HALF_UP);
            }
            return ZERO_RATE;
        }
        return sourceRow.getLifetimePdRate().divide(BigDecimal.valueOf(remainingCount), 10, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateCumulativePdRate(ProvisionEclCashflowSourceRow sourceRow, int remainingCount, int periodNo) {
        if ("STAGE1".equals(sourceRow.getStageCode())) {
            if (remainingCount == 1) {
                return sourceRow.getOneYearPdRate().setScale(10, RoundingMode.HALF_UP);
            }
            if (monthsCeil(sourceRow.getBaseDate(), sourceRow.getCashflowDate()) <= 12) {
                BigDecimal cumulativePd = sourceRow.getOneYearPdRate()
                        .divide(TWELVE, 10, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(periodNo), MC);
                return min(sourceRow.getOneYearPdRate(), cumulativePd).setScale(10, RoundingMode.HALF_UP);
            }
            return sourceRow.getOneYearPdRate().setScale(10, RoundingMode.HALF_UP);
        }

        BigDecimal cumulativePd = sourceRow.getLifetimePdRate()
                .divide(BigDecimal.valueOf(remainingCount), 10, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(periodNo), MC);
        return min(sourceRow.getLifetimePdRate(), cumulativePd).setScale(10, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateDiscountFactor(BigDecimal monthlyEir, LocalDate baseDate, LocalDate cashflowDate) {
        BigDecimal rate = nullToZero(monthlyEir);
        if (rate.compareTo(BigDecimal.ZERO) == 0) {
            return ONE;
        }

        int months = monthsCeil(baseDate, cashflowDate);
        double factor = Math.pow(BigDecimal.ONE.add(rate, MC).doubleValue(), -months);
        return BigDecimal.valueOf(factor).setScale(10, RoundingMode.HALF_UP);
    }

    private int monthsCeil(LocalDate baseDate, LocalDate cashflowDate) {
        if (!cashflowDate.isAfter(baseDate)) {
            return 1;
        }

        long wholeMonths = ChronoUnit.MONTHS.between(baseDate, cashflowDate);
        LocalDate shiftedDate = baseDate.plusMonths(wholeMonths);
        if (shiftedDate.isBefore(cashflowDate)) {
            wholeMonths++;
        }
        return Math.max(1, Math.toIntExact(wholeMonths));
    }

    private List<List<ProvisionEclCashflowSourceRow>> groupByTarget(List<ProvisionEclCashflowSourceRow> sourceRows) {
        List<List<ProvisionEclCashflowSourceRow>> groupedRows = new ArrayList<>();
        List<ProvisionEclCashflowSourceRow> currentRows = new ArrayList<>();
        Long currentTargetId = null;

        for (ProvisionEclCashflowSourceRow sourceRow : sourceRows) {
            if (currentTargetId != null && !currentTargetId.equals(sourceRow.getTargetId())) {
                groupedRows.add(currentRows);
                currentRows = new ArrayList<>();
            }
            currentRows.add(sourceRow);
            currentTargetId = sourceRow.getTargetId();
        }

        if (!currentRows.isEmpty()) {
            groupedRows.add(currentRows);
        }
        return groupedRows;
    }

    private BigDecimal nullToZero(BigDecimal value) {
        return value == null ? ZERO_RATE : value;
    }

    private BigDecimal min(BigDecimal left, BigDecimal right) {
        return left.compareTo(right) <= 0 ? left : right;
    }
}
