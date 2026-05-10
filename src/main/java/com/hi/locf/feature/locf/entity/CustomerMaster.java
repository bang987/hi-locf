package com.hi.locf.feature.locf.entity;

import java.time.LocalDateTime;

public class CustomerMaster {

    private Long customerId;
    private String customerNo;
    private String customerName;
    private String customerType;
    private String segmentCode;
    private String statusCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CustomerMaster create(String customerNo, String customerName, String customerType, String segmentCode) {
        CustomerMaster customer = new CustomerMaster();
        customer.customerNo = customerNo;
        customer.customerName = customerName;
        customer.customerType = customerType;
        customer.segmentCode = segmentCode;
        customer.statusCode = "ACTIVE";
        customer.createdAt = LocalDateTime.now();
        customer.updatedAt = LocalDateTime.now();
        return customer;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getSegmentCode() {
        return segmentCode;
    }

    public void setSegmentCode(String segmentCode) {
        this.segmentCode = segmentCode;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
