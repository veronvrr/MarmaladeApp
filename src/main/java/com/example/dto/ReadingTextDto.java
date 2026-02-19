package com.example.dto;

import java.util.List;

public class ReadingTextDto {
    private Long id;
    private String title;
    private String content;
    private String level;
    private String shortPreview;        // небольшой превью
    private List<ReadingTaskDto> tasks; // для страницы просмотра

    public ReadingTextDto() {}

    // геттеры / сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    public String getShortPreview() { return shortPreview; }
    public void setShortPreview(String shortPreview) { this.shortPreview = shortPreview; }
    public List<ReadingTaskDto> getTasks() { return tasks; }
    public void setTasks(List<ReadingTaskDto> tasks) { this.tasks = tasks; }
}

