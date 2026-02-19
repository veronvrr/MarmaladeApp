
package com.example.dto;

public class SubmittedAnswer {
    private final String questionId;
    private final String answer;

    public SubmittedAnswer(String questionId, String answer) {
        this.questionId = questionId;
        this.answer = answer;
    }

    public String getQuestionId() { return questionId; }
    public String getAnswer() { return answer; }
}
