package com.hi.locf.feature.locf.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hi.locf.feature.locf.entity.LocfCashflowBase;
import com.hi.locf.feature.locf.entity.LocfEirResult;
import com.hi.locf.feature.locf.entity.LocfTargetContract;

@Service
public class LocfEirServiceImpl implements LocfEirService {

    private static final MathContext MC = new MathContext(20, RoundingMode.HALF_UP);
    private static final BigDecimal ZERO = BigDecimal.ZERO;
    private static final BigDecimal ONE = BigDecimal.ONE;
    private static final BigDecimal TWELVE = BigDecimal.valueOf(12L);
    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100L);

    @Override
    public LocfEirResult calculateEir(Long batchExecutionId, LocfTargetContract contract, List<LocfCashflowBase> cashflows) {
        // 최초 장부가 = 원금 - 취급수수료 + 직접원가
        BigDecimal initialCarryingAmount = contract.getPrincipalAmt()
                .subtract(contract.getFeeAmt(), MC)
                .add(contract.getDirectCostAmt(), MC)
                .setScale(2, RoundingMode.HALF_UP);

        // 미래 약정 현금흐름 현재가치가 최초 장부가와 같아지는 월 할인율을 찾는다.
        BigDecimal monthlyEir = solveMonthlyEffectiveRate(initialCarryingAmount, cashflows);
        // 화면과 저장용으로는 연 EIR도 함께 관리한다.
        BigDecimal annualEir = monthlyEir.multiply(TWELVE, MC)
                .multiply(HUNDRED, MC)
                .setScale(10, RoundingMode.HALF_UP);

        return LocfEirResult.create(
                batchExecutionId,
                contract.getContractId(),
                contract.getContractNo(),
                initialCarryingAmount,
                monthlyEir.setScale(10, RoundingMode.HALF_UP),
                annualEir
        );
    }

    private BigDecimal solveMonthlyEffectiveRate(BigDecimal initialCarryingAmount, List<LocfCashflowBase> cashflows) {
        // 닫힌 수식으로 풀지 않고 이분탐색으로 월 EIR을 수렴시킨다.
        BigDecimal low = ZERO;
        BigDecimal high = ONE;
        for (int i = 0; i < 200; i++) {
            BigDecimal mid = low.add(high, MC).divide(BigDecimal.valueOf(2L), MC);
            BigDecimal pv = presentValue(mid, cashflows);
            if (pv.compareTo(initialCarryingAmount) > 0) {
                low = mid;
            } else {
                high = mid;
            }
        }
        return low;
    }

    private BigDecimal presentValue(BigDecimal monthlyRate, List<LocfCashflowBase> cashflows) {
        // 각 회차 납입액을 월 할인율로 할인해 현재가치를 계산한다.
        BigDecimal pv = ZERO;
        for (LocfCashflowBase row : cashflows) {
            int period = row.getInstallmentNo().intValue();
            BigDecimal divisor = ONE.add(monthlyRate, MC).pow(period, MC);
            pv = pv.add(row.getScheduledPaymentAmt().divide(divisor, MC), MC);
        }
        return pv;
    }
}
