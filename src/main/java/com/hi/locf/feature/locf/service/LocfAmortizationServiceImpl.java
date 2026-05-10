package com.hi.locf.feature.locf.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hi.locf.feature.locf.entity.LocfAmortizationDetail;
import com.hi.locf.feature.locf.entity.LocfCashflowBase;
import com.hi.locf.feature.locf.entity.LocfEirResult;
import com.hi.locf.feature.locf.entity.LocfTargetContract;

@Service
public class LocfAmortizationServiceImpl implements LocfAmortizationService {

    private static final MathContext MC = new MathContext(20, RoundingMode.HALF_UP);
    private static final BigDecimal ZERO = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

    @Override
    public List<LocfAmortizationDetail> calculateAmortization(
            Long batchExecutionId,
            LocfTargetContract contract,
            LocfEirResult eirResult,
            List<LocfCashflowBase> cashflows
    ) {
        // 회차별 상각상세를 순차 계산하므로 실행 중 잔액을 로컬 변수로 유지한다.
        List<LocfAmortizationDetail> details = new ArrayList<>();

        BigDecimal openingPrincipalBal = contract.getPrincipalAmt().setScale(2, RoundingMode.HALF_UP);
        BigDecimal openingCarryingAmt = eirResult.getInitialCarryingAmount().setScale(2, RoundingMode.HALF_UP);
        BigDecimal feeAmt = contract.getFeeAmt().setScale(2, RoundingMode.HALF_UP);
        BigDecimal directCostAmt = contract.getDirectCostAmt().setScale(2, RoundingMode.HALF_UP);
        BigDecimal deferredNet = feeAmt.subtract(directCostAmt, MC);

        for (LocfCashflowBase cashflow : cashflows) {
            // 유효이자수익은 기초 장부가 x 월 EIR 이다.
            BigDecimal effectiveInterestRevenue = openingCarryingAmt.multiply(eirResult.getMonthlyEir(), MC)
                    .setScale(2, RoundingMode.HALF_UP);
            // 약정이자와 유효이자수익의 차이가 순상각금액이다.
            BigDecimal netAmortizationAmt = effectiveInterestRevenue.subtract(cashflow.getScheduledInterestAmt(), MC)
                    .setScale(2, RoundingMode.HALF_UP);

            BigDecimal feeAmortizationAmt = ZERO;
            BigDecimal costAmortizationAmt = ZERO;
            if (deferredNet.compareTo(BigDecimal.ZERO) != 0) {
                feeAmortizationAmt = netAmortizationAmt.multiply(feeAmt, MC)
                        .divide(deferredNet, 2, RoundingMode.HALF_UP);
                costAmortizationAmt = netAmortizationAmt.multiply(directCostAmt, MC)
                        .divide(deferredNet, 2, RoundingMode.HALF_UP);
            }

            BigDecimal closingPrincipalBal = openingPrincipalBal.subtract(cashflow.getScheduledPrincipalAmt(), MC)
                    .setScale(2, RoundingMode.HALF_UP);
            // 장부가는 유효이자수익을 더하고 실제 수취 현금흐름을 차감해 굴린다.
            BigDecimal closingCarryingAmt = openingCarryingAmt.add(effectiveInterestRevenue, MC)
                    .subtract(cashflow.getScheduledPaymentAmt(), MC)
                    .setScale(2, RoundingMode.HALF_UP);

            LocfAmortizationDetail detail = new LocfAmortizationDetail();
            detail.setBatchExecutionId(batchExecutionId);
            detail.setContractId(contract.getContractId());
            detail.setContractNo(contract.getContractNo());
            detail.setInstallmentNo(cashflow.getInstallmentNo());
            detail.setPaymentDate(cashflow.getPaymentDate());
            detail.setOpeningPrincipalBal(openingPrincipalBal);
            detail.setOpeningCarryingAmt(openingCarryingAmt);
            detail.setScheduledPaymentAmt(cashflow.getScheduledPaymentAmt());
            detail.setScheduledPrincipalAmt(cashflow.getScheduledPrincipalAmt());
            detail.setScheduledInterestAmt(cashflow.getScheduledInterestAmt());
            detail.setEffectiveInterestRevenue(effectiveInterestRevenue);
            detail.setFeeAmortizationAmt(feeAmortizationAmt);
            detail.setCostAmortizationAmt(costAmortizationAmt);
            detail.setNetAmortizationAmt(netAmortizationAmt);
            detail.setClosingPrincipalBal(closingPrincipalBal);
            detail.setClosingCarryingAmt(closingCarryingAmt);
            details.add(detail);

            openingPrincipalBal = closingPrincipalBal;
            openingCarryingAmt = closingCarryingAmt;
        }

        if (!details.isEmpty()) {
            LocfAmortizationDetail last = details.get(details.size() - 1);
            // 마지막 회차는 반올림 오차를 제거하기 위해 잔액을 0으로 맞춘다.
            last.setClosingPrincipalBal(ZERO);
            last.setClosingCarryingAmt(ZERO);
        }

        return details;
    }
}
