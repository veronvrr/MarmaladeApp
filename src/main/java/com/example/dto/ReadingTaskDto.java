// src/main/java/com/example/dto/ReadingTaskDto.java
package com.example.dto;

import java.util.List;

public class ReadingTaskDto {
    private Long id;
    private String type; // "tf", "mcq", "fill", ...
    private String prompt;
    private List<String> options; // null -> текстовый ответ
    private String answer;

    public ReadingTaskDto() {}

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
