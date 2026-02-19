package com.example.dto;

import java.util.List;

public class ListeningCheckRequest {
    private Long audioId;
    private List<ListeningSubmittedAnswerDto> answers;

    public ListeningCheckRequest() {}
    public Long getAudioId() { return audioId; }
    public void setAudioId(Long audioId) { this.audioId = audioId; }
    public List<ListeningSubmittedAnswerDto> getAnswers() { return answers; }
    public void setAnswers(List<ListeningSubmittedAnswerDto> answers) { this.answers = answers; }
}
