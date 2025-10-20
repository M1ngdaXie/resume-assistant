package com.assistant.doitou.dto.analytics;

import java.time.LocalDate;

public class DailyMetric {
    private LocalDate date;
    private Long activeUsers;
    private Long totalMessages;

    // Constructors
    public DailyMetric() {}

    public DailyMetric(LocalDate date, Long activeUsers, Long totalMessages) {
        this.date = date;
        this.activeUsers = activeUsers;
        this.totalMessages = totalMessages;
    }

    // Getters and Setters
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(Long activeUsers) {
        this.activeUsers = activeUsers;
    }

    public Long getTotalMessages() {
        return totalMessages;
    }

    public void setTotalMessages(Long totalMessages) {
        this.totalMessages = totalMessages;
    }
}