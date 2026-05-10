package com.hi.locf.feature.locf.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.hi.locf.feature.locf.entity.CustomerMaster;
import com.hi.locf.feature.locf.entity.LoanBalanceSource;
import com.hi.locf.feature.locf.entity.LoanContractSource;
import com.hi.locf.feature.locf.entity.LoanRateSource;
import com.hi.locf.feature.locf.entity.LoanRepaymentScheduleSource;

@Mapper
public interface LocfSourceDataMapper {

    long countContracts();

    int insertCustomer(CustomerMaster customer);

    int insertContract(LoanContractSource contract);

    int insertRate(LoanRateSource rate);

    int insertRepaymentSchedule(LoanRepaymentScheduleSource schedule);

    int insertBalance(LoanBalanceSource balance);

    List<LoanRepaymentScheduleSource> findRepaymentSchedulesByContractId(@Param("contractId") Long contractId);
}
