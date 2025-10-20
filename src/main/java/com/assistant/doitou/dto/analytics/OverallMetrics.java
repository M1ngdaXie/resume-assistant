package com.assistant.doitou.dto.analytics;

public class OverallMetrics {
    private Long totalUsers;
    private Long totalConversations;
    private Long totalMessages;
    private Long emailCaptures;
    private Long unknownQuestions;
    private Double averageSessionLength; // in minutes

    // Constructors
    public OverallMetrics() {}

    public OverallMetrics(Long totalUsers, Long totalConversations, Long totalMessages,
                          Long emailCaptures, Long unknownQuestions, Double averageSessionLength) {
        this.totalUsers = totalUsers;
        this.totalConversations = totalConversations;
        this.totalMessages = totalMessages;
        this.emailCaptures = emailCaptures;
        this.unknownQuestions = unknownQuestions;
        this.averageSessionLength = averageSessionLength;
    }

    // Getters and Setters
    public Long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(Long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public Long getTotalConversations() {
        return totalConversations;
    }

    public void setTotalConversations(Long totalConversations) {
        this.totalConversations = totalConversations;
    }

    public Long getTotalMessages() {
        return totalMessages;
    }

    public void setTotalMessages(Long totalMessages) {
        this.totalMessages = totalMessages;
    }

    public Long getEmailCaptures() {
        return emailCaptures;
    }

    public void setEmailCaptures(Long emailCaptures) {
        this.emailCaptures = emailCaptures;
    }

    public Long getUnknownQuestions() {
        return unknownQuestions;
    }

    public void setUnknownQuestions(Long unknownQuestions) {
        this.unknownQuestions = unknownQuestions;
    }

    public Double getAverageSessionLength() {
        return averageSessionLength;
    }

    public void setAverageSessionLength(Double averageSessionLength) {
        this.averageSessionLength = averageSessionLength;
    }
}