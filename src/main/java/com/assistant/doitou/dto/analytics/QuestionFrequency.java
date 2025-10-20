package com.assistant.doitou.dto.analytics;

public class QuestionFrequency {
    private String question;
    private Long count;

    // Constructors
    public QuestionFrequency() {}

    public QuestionFrequency(String question, Long count) {
        this.question = question;
        this.count = count;
    }

    // Getters and Setters
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}