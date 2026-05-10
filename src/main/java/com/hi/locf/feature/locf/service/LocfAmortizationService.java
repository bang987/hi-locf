package com.hi.locf.feature.locf.service;

import java.util.List;

import com.hi.locf.feature.locf.entity.LocfAmortizationDetail;
import com.hi.locf.feature.locf.entity.LocfCashflowBase;
import com.hi.locf.feature.locf.entity.LocfEirResult;
import com.hi.locf.feature.locf.entity.LocfTargetContract;

public interface LocfAmortizationService {

    List<LocfAmortizationDetail> calculateAmortization(
            Long batchExecutionId,
            LocfTargetContract contract,
            LocfEirResult eirResult,
            List<LocfCashflowBase> cashflows
    );
}
