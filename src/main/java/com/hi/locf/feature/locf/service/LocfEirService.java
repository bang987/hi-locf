package com.hi.locf.feature.locf.service;

import java.util.List;

import com.hi.locf.feature.locf.entity.LocfCashflowBase;
import com.hi.locf.feature.locf.entity.LocfEirResult;
import com.hi.locf.feature.locf.entity.LocfTargetContract;

public interface LocfEirService {

    LocfEirResult calculateEir(Long batchExecutionId, LocfTargetContract contract, List<LocfCashflowBase> cashflows);
}
