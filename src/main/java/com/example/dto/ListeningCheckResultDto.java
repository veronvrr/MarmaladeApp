package com.example.dto;

import java.util.Map;

public class ListeningCheckResultDto {
    private int correct;
    private int total;
    private Map<String, Boolean> perQuestion; // questionId -> true/false

    public ListeningCheckResultDto() {}
    public ListeningCheckResultDto(int correct, int total, Map<String, Boolean> perQuestion) {
        this.correct = correct; this.total = total; this.perQuestion = perQuestion;
    }
    public int getCorrect() { return correct; }
    public int getTotal() { return total; }
    public Map<String, Boolean> getPerQuestion() { return perQuestion; }
    public void setCorrect(int correct) { this.correct = correct; }
    public void setTotal(int total) { this.total = total; }
    public void setPerQuestion(Map<String, Boolean> perQuestion) { this.perQuestion = perQuestion; }
}
