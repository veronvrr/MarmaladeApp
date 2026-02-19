package com.example.dto;

import java.util.List;

public class TextTaskDto {
    private Long id;
    private String type;
    private String prompt;
    private List<String> options; // null for fill/short/truefalse (we may still use options for TF)
    private String answer;

    public TextTaskDto() {}
    public TextTaskDto(Long id, String type, String prompt, List<String> options, String answer) {
        this.id = id; this.type = type; this.prompt = prompt; this.options = options; this.answer = answer;
    }
    // getters/setters ...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getPrompt() { return prompt; }
    public void setPrompt(String prompt) { this.prompt = prompt; }
    public List<String> getOptions() { return options; }
    public void setOptions(List<String> options) { this.options = options; }
    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }
}
