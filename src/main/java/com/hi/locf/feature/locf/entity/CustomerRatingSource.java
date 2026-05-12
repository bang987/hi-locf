package com.hi.locf.feature.locf.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class CustomerRatingSource {

    private Long ratingId;
    private Long customerId;
    private LocalDate baseDate;
    private String ratingGrade;
    private BigDecimal oneYearPdRate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CustomerRatingSource create(Long customerId, LocalDate baseDate, String ratingGrade, BigDecimal oneYearPdRate) {
        LocalDateTime now = LocalDateTime.now();
        CustomerRatingSource rating = new CustomerRatingSource();
        rating.customerId = customerId;
        rating.baseDate = baseDate;
        rating.ratingGrade = ratingGrade;
        rating.oneYearPdRate = oneYearPdRate;
        rating.createdAt = now;
        rating.updatedAt = now;
        return rating;
    }

    public Long getRatingId() { return ratingId; }
    public void setRatingId(Long ratingId) { this.ratingId = ratingId; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public LocalDate getBaseDate() { return baseDate; }
    public void setBaseDate(LocalDate baseDate) { this.baseDate = baseDate; }
    public String getRatingGrade() { return ratingGrade; }
    public void setRatingGrade(String ratingGrade) { this.ratingGrade = ratingGrade; }
    public BigDecimal getOneYearPdRate() { return oneYearPdRate; }
    public void setOneYearPdRate(BigDecimal oneYearPdRate) { this.oneYearPdRate = oneYearPdRate; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
