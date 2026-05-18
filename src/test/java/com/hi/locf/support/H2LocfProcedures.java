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

    public static void clearProvisionBatchData(Connection connection, Long batchExecutionId) throws Exception {
        executeDelete(connection, "DELETE FROM PRV_ECL_RESULT_SUMMARY WHERE BATCH_EXECUTION_ID = ?", batchExecutionId);
        executeDelete(connection, "DELETE FROM PRV_ECL_RESULT_DTL WHERE BATCH_EXECUTION_ID = ?", batchExecutionId);
        executeDelete(connection, "DELETE FROM PRV_ECL_CASHFLOW_DTL WHERE BATCH_EXECUTION_ID = ?", batchExecutionId);
        executeDelete(connection, "DELETE FROM PRV_LGD_RESULT WHERE BATCH_EXECUTION_ID = ?", batchExecutionId);
        executeDelete(connection, "DELETE FROM PRV_PD_RESULT WHERE BATCH_EXECUTION_ID = ?", batchExecutionId);
        executeDelete(connection, "DELETE FROM PRV_EAD_RESULT WHERE BATCH_EXECUTION_ID = ?", batchExecutionId);
        executeDelete(connection, "DELETE FROM PRV_STAGE_RESULT WHERE BATCH_EXECUTION_ID = ?", batchExecutionId);
        executeDelete(connection, "DELETE FROM PRV_TARGET_CONTRACT WHERE BATCH_EXECUTION_ID = ?", batchExecutionId);
    }

    public static void insertProvisionTargetContracts(Connection connection, Long batchExecutionId, Date baseDate) throws Exception {
        String sql =
                "INSERT INTO PRV_TARGET_CONTRACT (" +
                "TARGET_ID, BATCH_EXECUTION_ID, BASE_DATE, CONTRACT_ID, CONTRACT_NO, CUSTOMER_ID, CUSTOMER_NAME, PRODUCT_CODE, " +
                "OUTSTANDING_PRINCIPAL_AMT, CARRYING_AMOUNT, DELINQUENCY_DAYS, DEFAULT_YN, RATING_GRADE, ONE_YEAR_PD_RATE, COLLATERAL_VALUE_AMT, RECOVERY_RATE) " +
                "SELECT NEXT VALUE FOR SQ_PRV_TARGET_CONTRACT, ?, ?, c.CONTRACT_ID, c.CONTRACT_NO, c.CUSTOMER_ID, cm.CUSTOMER_NAME, c.PRODUCT_CODE, " +
                "b.OUTSTANDING_PRINCIPAL_AMT, COALESCE((" +
                "  SELECT d.CLOSING_CARRYING_AMT FROM LOCF_AMORTIZATION_DTL d " +
                "   WHERE d.CONTRACT_ID = c.CONTRACT_ID AND d.PAYMENT_DATE <= ? " +
                "   ORDER BY d.PAYMENT_DATE DESC, d.INSTALLMENT_NO DESC FETCH FIRST 1 ROWS ONLY" +
                "), COALESCE((" +
                "  SELECT e.INITIAL_CARRYING_AMOUNT FROM LOCF_EIR_RESULT e " +
                "   WHERE e.CONTRACT_ID = c.CONTRACT_ID ORDER BY e.EIR_RESULT_ID DESC FETCH FIRST 1 ROWS ONLY" +
                "), b.OUTSTANDING_PRINCIPAL_AMT)), " +
                "COALESCE(d.DELINQUENCY_DAYS, 0), COALESCE(d.DEFAULT_YN, 'N'), COALESCE(rg.RATING_GRADE, 'B'), COALESCE(rg.ONE_YEAR_PD_RATE, 0.0300), " +
                "COALESCE(cl.COLLATERAL_VALUE_AMT, 0), COALESCE(cl.RECOVERY_RATE, 0.20) " +
                "FROM LOAN_CONTRACT c " +
                "JOIN CUSTOMER_MASTER cm ON cm.CUSTOMER_ID = c.CUSTOMER_ID " +
                "JOIN LOAN_BALANCE b ON b.CONTRACT_ID = c.CONTRACT_ID AND b.BASE_DATE = ? " +
                "LEFT JOIN LOAN_DELINQUENCY d ON d.CONTRACT_ID = c.CONTRACT_ID AND d.BASE_DATE = ? " +
                "LEFT JOIN CUSTOMER_RATING rg ON rg.CUSTOMER_ID = c.CUSTOMER_ID AND rg.BASE_DATE = ? " +
                "LEFT JOIN LOAN_COLLATERAL cl ON cl.CONTRACT_ID = c.CONTRACT_ID AND cl.BASE_DATE = ? " +
                "WHERE c.STATUS_CODE = 'ACTIVE'";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, batchExecutionId);
            preparedStatement.setDate(2, baseDate);
            preparedStatement.setDate(3, baseDate);
            preparedStatement.setDate(4, baseDate);
            preparedStatement.setDate(5, baseDate);
            preparedStatement.setDate(6, baseDate);
            preparedStatement.setDate(7, baseDate);
            preparedStatement.executeUpdate();
        }
    }

    public static void calculateProvisionStage(Connection connection, Long batchExecutionId) throws Exception {
        String sql =
                "INSERT INTO PRV_STAGE_RESULT (STAGE_RESULT_ID, BATCH_EXECUTION_ID, TARGET_ID, CONTRACT_ID, CONTRACT_NO, STAGE_CODE, STAGE_REASON) " +
                "SELECT NEXT VALUE FOR SQ_PRV_STAGE_RESULT, BATCH_EXECUTION_ID, TARGET_ID, CONTRACT_ID, CONTRACT_NO, " +
                "CASE WHEN DEFAULT_YN = 'Y' OR DELINQUENCY_DAYS >= 90 THEN 'STAGE3' " +
                "WHEN DELINQUENCY_DAYS >= 30 THEN 'STAGE2' ELSE 'STAGE1' END, " +
                "CASE WHEN DEFAULT_YN = 'Y' THEN '부도차주' " +
                "WHEN DELINQUENCY_DAYS >= 90 THEN '90일이상연체' " +
                "WHEN DELINQUENCY_DAYS >= 30 THEN '30일이상연체' ELSE '정상계약' END " +
                "FROM PRV_TARGET_CONTRACT WHERE BATCH_EXECUTION_ID = ?";
        executeInsertWithBatchId(connection, sql, batchExecutionId);
    }

    public static void calculateProvisionEad(Connection connection, Long batchExecutionId) throws Exception {
        String sql =
                "INSERT INTO PRV_EAD_RESULT (EAD_RESULT_ID, BATCH_EXECUTION_ID, TARGET_ID, CONTRACT_ID, CONTRACT_NO, CARRYING_AMOUNT, EAD_AMOUNT) " +
                "SELECT NEXT VALUE FOR SQ_PRV_EAD_RESULT, BATCH_EXECUTION_ID, TARGET_ID, CONTRACT_ID, CONTRACT_NO, CARRYING_AMOUNT, CARRYING_AMOUNT " +
                "FROM PRV_TARGET_CONTRACT WHERE BATCH_EXECUTION_ID = ?";
        executeInsertWithBatchId(connection, sql, batchExecutionId);
    }

    public static void calculateProvisionPd(Connection connection, Long batchExecutionId) throws Exception {
        String sql =
                "INSERT INTO PRV_PD_RESULT (PD_RESULT_ID, BATCH_EXECUTION_ID, TARGET_ID, CONTRACT_ID, CONTRACT_NO, STAGE_CODE, ONE_YEAR_PD_RATE, LIFETIME_PD_RATE) " +
                "SELECT NEXT VALUE FOR SQ_PRV_PD_RESULT, t.BATCH_EXECUTION_ID, t.TARGET_ID, t.CONTRACT_ID, t.CONTRACT_NO, s.STAGE_CODE, t.ONE_YEAR_PD_RATE, " +
                "CASE WHEN s.STAGE_CODE = 'STAGE1' THEN t.ONE_YEAR_PD_RATE " +
                "WHEN s.STAGE_CODE = 'STAGE2' THEN LEAST(t.ONE_YEAR_PD_RATE * 2.5, 1) " +
                "ELSE LEAST(t.ONE_YEAR_PD_RATE * 4, 1) END " +
                "FROM PRV_TARGET_CONTRACT t JOIN PRV_STAGE_RESULT s ON s.TARGET_ID = t.TARGET_ID " +
                "WHERE t.BATCH_EXECUTION_ID = ?";
        executeInsertWithBatchId(connection, sql, batchExecutionId);
    }

    public static void calculateProvisionLgd(Connection connection, Long batchExecutionId) throws Exception {
        String sql =
                "INSERT INTO PRV_LGD_RESULT (LGD_RESULT_ID, BATCH_EXECUTION_ID, TARGET_ID, CONTRACT_ID, CONTRACT_NO, RECOVERY_RATE, LGD_RATE) " +
                "SELECT NEXT VALUE FOR SQ_PRV_LGD_RESULT, BATCH_EXECUTION_ID, TARGET_ID, CONTRACT_ID, CONTRACT_NO, RECOVERY_RATE, " +
                "CASE WHEN (1 - RECOVERY_RATE) < 0.15 THEN 0.15 ELSE (1 - RECOVERY_RATE) END " +
                "FROM PRV_TARGET_CONTRACT WHERE BATCH_EXECUTION_ID = ?";
        executeInsertWithBatchId(connection, sql, batchExecutionId);
    }

    public static void buildProvisionEclCashflow(Connection connection, Long batchExecutionId) throws Exception {
        String sql =
                "INSERT INTO PRV_ECL_CASHFLOW_DTL (" +
                "ECL_CASHFLOW_ID, BATCH_EXECUTION_ID, TARGET_ID, CONTRACT_ID, CONTRACT_NO, INSTALLMENT_NO, CASHFLOW_DATE, " +
                "BEGINNING_EAD_AMOUNT, EXPECTED_PRINCIPAL_AMT, EXPECTED_INTEREST_AMT, ENDING_EAD_AMOUNT, " +
                "MARGINAL_PD_RATE, CUMULATIVE_PD_RATE, LGD_RATE, DISCOUNT_RATE, DISCOUNT_FACTOR, PERIOD_ECL_AMOUNT, PV_ECL_AMOUNT) " +
                "SELECT NEXT VALUE FOR SQ_PRV_ECL_CASHFLOW_DTL, t.BATCH_EXECUTION_ID, t.TARGET_ID, t.CONTRACT_ID, t.CONTRACT_NO, " +
                "1, t.BASE_DATE, e.EAD_AMOUNT, 0, 0, e.EAD_AMOUNT, " +
                "CASE WHEN s.STAGE_CODE = 'STAGE1' THEN p.ONE_YEAR_PD_RATE ELSE p.LIFETIME_PD_RATE END, " +
                "CASE WHEN s.STAGE_CODE = 'STAGE1' THEN p.ONE_YEAR_PD_RATE ELSE p.LIFETIME_PD_RATE END, " +
                "l.LGD_RATE, 0, 1, " +
                "ROUND(e.EAD_AMOUNT * CASE WHEN s.STAGE_CODE = 'STAGE1' THEN p.ONE_YEAR_PD_RATE ELSE p.LIFETIME_PD_RATE END * l.LGD_RATE, 2), " +
                "ROUND(e.EAD_AMOUNT * CASE WHEN s.STAGE_CODE = 'STAGE1' THEN p.ONE_YEAR_PD_RATE ELSE p.LIFETIME_PD_RATE END * l.LGD_RATE, 2) " +
                "FROM PRV_TARGET_CONTRACT t " +
                "JOIN PRV_STAGE_RESULT s ON s.TARGET_ID = t.TARGET_ID " +
                "JOIN PRV_EAD_RESULT e ON e.TARGET_ID = t.TARGET_ID " +
                "JOIN PRV_PD_RESULT p ON p.TARGET_ID = t.TARGET_ID " +
                "JOIN PRV_LGD_RESULT l ON l.TARGET_ID = t.TARGET_ID " +
                "WHERE t.BATCH_EXECUTION_ID = ?";
        executeInsertWithBatchId(connection, sql, batchExecutionId);
    }

    public static void calculateProvisionEcl(Connection connection, Long batchExecutionId) throws Exception {
        String sql =
                "INSERT INTO PRV_ECL_RESULT_DTL (" +
                "RESULT_DTL_ID, BATCH_EXECUTION_ID, BASE_DATE, CONTRACT_ID, CONTRACT_NO, CUSTOMER_NAME, PRODUCT_CODE, STAGE_CODE, STAGE_REASON, " +
                "CARRYING_AMOUNT, EAD_AMOUNT, ONE_YEAR_PD_RATE, LIFETIME_PD_RATE, LGD_RATE, RECOVERY_RATE, ECL_AMOUNT) " +
                "SELECT NEXT VALUE FOR SQ_PRV_ECL_RESULT_DTL, t.BATCH_EXECUTION_ID, t.BASE_DATE, t.CONTRACT_ID, t.CONTRACT_NO, t.CUSTOMER_NAME, t.PRODUCT_CODE, s.STAGE_CODE, s.STAGE_REASON, " +
                "e.CARRYING_AMOUNT, e.EAD_AMOUNT, p.ONE_YEAR_PD_RATE, p.LIFETIME_PD_RATE, l.LGD_RATE, l.RECOVERY_RATE, " +
                "COALESCE(cf.ECL_AMOUNT, 0) " +
                "FROM PRV_TARGET_CONTRACT t " +
                "JOIN PRV_STAGE_RESULT s ON s.TARGET_ID = t.TARGET_ID " +
                "JOIN PRV_EAD_RESULT e ON e.TARGET_ID = t.TARGET_ID " +
                "JOIN PRV_PD_RESULT p ON p.TARGET_ID = t.TARGET_ID " +
                "JOIN PRV_LGD_RESULT l ON l.TARGET_ID = t.TARGET_ID " +
                "LEFT JOIN (SELECT BATCH_EXECUTION_ID, TARGET_ID, SUM(PV_ECL_AMOUNT) AS ECL_AMOUNT FROM PRV_ECL_CASHFLOW_DTL " +
                "WHERE BATCH_EXECUTION_ID = ? GROUP BY BATCH_EXECUTION_ID, TARGET_ID) cf ON cf.TARGET_ID = t.TARGET_ID " +
                "WHERE t.BATCH_EXECUTION_ID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, batchExecutionId);
            preparedStatement.setLong(2, batchExecutionId);
            preparedStatement.executeUpdate();
        }
    }

    public static void insertProvisionResultSummary(Connection connection, Long batchExecutionId) throws Exception {
        String sql =
                "INSERT INTO PRV_ECL_RESULT_SUMMARY (" +
                "SUMMARY_ID, BATCH_EXECUTION_ID, BASE_DATE, PRODUCT_CODE, STAGE_CODE, CONTRACT_COUNT, TOTAL_CARRYING_AMOUNT, TOTAL_EAD_AMOUNT, TOTAL_ECL_AMOUNT, CREATED_AT) " +
                "SELECT NEXT VALUE FOR SQ_PRV_ECL_RESULT_SUMMARY, BATCH_EXECUTION_ID, BASE_DATE, PRODUCT_CODE, STAGE_CODE, COUNT(*), " +
                "SUM(CARRYING_AMOUNT), SUM(EAD_AMOUNT), SUM(ECL_AMOUNT), CURRENT_TIMESTAMP " +
                "FROM PRV_ECL_RESULT_DTL WHERE BATCH_EXECUTION_ID = ? " +
                "GROUP BY BATCH_EXECUTION_ID, BASE_DATE, PRODUCT_CODE, STAGE_CODE";
        executeInsertWithBatchId(connection, sql, batchExecutionId);
    }

    private static void executeDelete(Connection connection, String sql, Long batchExecutionId) throws Exception {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, batchExecutionId);
            preparedStatement.executeUpdate();
        }
    }

    private static void executeInsertWithBatchId(Connection connection, String sql, Long batchExecutionId) throws Exception {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, batchExecutionId);
            preparedStatement.executeUpdate();
        }
    }
}
