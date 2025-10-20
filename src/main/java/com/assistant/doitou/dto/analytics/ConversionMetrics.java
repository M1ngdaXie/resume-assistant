package com.assistant.doitou.dto.analytics;

public class ConversionMetrics {
    private Long totalUsers;
    private Long emailCaptures;
    private Double conversionRate; // percentage (0-100)

    // Constructors
    public ConversionMetrics() {}

    public ConversionMetrics(Long totalUsers, Long emailCaptures, Double conversionRate) {
        this.totalUsers = totalUsers;
        this.emailCaptures = emailCaptures;
        this.conversionRate = conversionRate;
    }

    // Getters and Setters
    public Long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(Long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public Long getEmailCaptures() {
        return emailCaptures;
    }

    public void setEmailCaptures(Long emailCaptures) {
        this.emailCaptures = emailCaptures;
    }

    public Double getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(Double conversionRate) {
        this.conversionRate = conversionRate;
    }
}