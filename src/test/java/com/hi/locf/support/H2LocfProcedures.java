package com.hi.locf.support;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;

public final class H2LocfProcedures {

    private H2LocfProcedures() {
    }

    public static void clearBatchData(Connection connection, Long batchExecutionId) throws Exception {
        executeDelete(connection, "DELETE FROM LOCF_RESULT_SUMMARY WHERE BATCH_EXECUTION_ID = ?", batchExecutionId);
        executeDelete(connection, "DELETE FROM LOCF_RESULT_HDR WHERE BATCH_EXECUTION_ID = ?", batchExecutionId);
        executeDelete(connection, "DELETE FROM LOCF_AMORTIZATION_DTL WHERE BATCH_EXECUTION_ID = ?", batchExecutionId);
        executeDelete(connection, "DELETE FROM LOCF_EIR_RESULT WHERE BATCH_EXECUTION_ID = ?", batchExecutionId);
        executeDelete(connection, "DELETE FROM LOCF_CASHFLOW_BASE WHERE BATCH_EXECUTION_ID = ?", batchExecutionId);
        executeDelete(connection, "DELETE FROM LOCF_TARGET_CONTRACT WHERE BATCH_EXECUTION_ID = ?", batchExecutionId);
    }

    public static void insertTargetContracts(Connection connection, Long batchExecutionId, Date baseDate) throws Exception {
        String sql =
                "INSERT INTO LOCF_TARGET_CONTRACT (" +
                "TARGET_ID, BATCH_EXECUTION_ID, CONTRACT_ID, CONTRACT_NO, CUSTOMER_ID, PRODUCT_CODE, " +
                "REPAYMENT_TYPE, PRINCIPAL_AMT, FEE_AMT, DIRECT_COST_AMT, ANNUAL_NOMINAL_RATE, " +
                "EXECUTION_DATE, MATURITY_DATE, TERM_IN_MONTHS) " +
                "SELECT NEXT VALUE FOR SQ_LOCF_TARGET_CONTRACT, ?, c.CONTRACT_ID, c.CONTRACT_NO, c.CUSTOMER_ID, c.PRODUCT_CODE, " +
                "c.REPAYMENT_TYPE, c.PRINCIPAL_AMT, c.FEE_AMT, c.DIRECT_COST_AMT, r.ANNUAL_NOMINAL_RATE, " +
                "c.EXECUTION_DATE, c.MATURITY_DATE, DATEDIFF('MONTH', c.EXECUTION_DATE, c.MATURITY_DATE) " +
                "FROM LOAN_CONTRACT c " +
                "JOIN LOAN_RATE r ON r.CONTRACT_ID = c.CONTRACT_ID " +
                "JOIN LOAN_BALANCE b ON b.CONTRACT_ID = c.CONTRACT_ID " +
                "WHERE c.STATUS_CODE = 'ACTIVE' " +
                "AND b.BASE_DATE = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, batchExecutionId);
            preparedStatement.setDate(2, baseDate);
            preparedStatement.executeUpdate();
        }
    }

    public static void insertResultSummary(Connection connection, Long batchExecutionId) throws Exception {
        String sql =
                "INSERT INTO LOCF_RESULT_SUMMARY (" +
                "SUMMARY_ID, BATCH_EXECUTION_ID, BASE_DATE, PRODUCT_CODE, CONTRACT_COUNT, " +
                "TOTAL_INITIAL_CARRYING_AMT, TOTAL_FINAL_CARRYING_AMT, TOTAL_EFFECTIVE_INTEREST_REV, CREATED_AT) " +
                "SELECT NEXT VALUE FOR SQ_LOCF_RESULT_SUMMARY, " +
                "summary_rows.BATCH_EXECUTION_ID, summary_rows.BASE_DATE, summary_rows.PRODUCT_CODE, summary_rows.CONTRACT_COUNT, " +
                "summary_rows.TOTAL_INITIAL_CARRYING_AMT, summary_rows.TOTAL_FINAL_CARRYING_AMT, summary_rows.TOTAL_EFFECTIVE_INTEREST_REV, CURRENT_TIMESTAMP " +
                "FROM (" +
                "SELECT h.BATCH_EXECUTION_ID, h.BASE_DATE, h.PRODUCT_CODE, COUNT(*) AS CONTRACT_COUNT, " +
                "SUM(h.INITIAL_CARRYING_AMOUNT) AS TOTAL_INITIAL_CARRYING_AMT, " +
                "SUM(h.FINAL_CARRYING_AMOUNT) AS TOTAL_FINAL_CARRYING_AMT, " +
                "SUM(h.TOTAL_EFFECTIVE_INTEREST_REV) AS TOTAL_EFFECTIVE_INTEREST_REV " +
                "FROM LOCF_RESULT_HDR h " +
                "WHERE h.BATCH_EXECUTION_ID = ? " +
                "GROUP BY h.BATCH_EXECUTION_ID, h.BASE_DATE, h.PRODUCT_CODE" +
                ") summary_rows";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, batchExecutionId);
            preparedStatement.executeUpdate();
        }
    }

    private static void executeDelete(Connection connection, String sql, Long batchExecutionId) throws Exception {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, batchExecutionId);
            preparedStatement.executeUpdate();
        }
    }
}
