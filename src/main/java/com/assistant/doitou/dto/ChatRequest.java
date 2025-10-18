package com.assistant.doitou.dto;

public class ChatRequest {
    private String userId;
    private String role;
    private String message;

    // Constructors
    public ChatRequest() {}

    public ChatRequest(String userId, String role, String message) {
        this.userId = userId;
        this.role = role;
        this.message = message;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}