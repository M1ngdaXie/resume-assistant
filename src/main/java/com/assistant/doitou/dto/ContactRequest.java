package com.assistant.doitou.dto;

public class ContactRequest {
    private String email;
    private String name;
    private String notes;

    // Constructors
    public ContactRequest() {}

    public ContactRequest(String email, String name, String notes) {
        this.email = email;
        this.name = name;
        this.notes = notes;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}