package com.assistant.doitou.dto;

public class QuestionRequest {
    private String question;

    // Constructors
    public QuestionRequest() {}

    public QuestionRequest(String question) {
        this.question = question;
    }

    // Getters and Setters
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
