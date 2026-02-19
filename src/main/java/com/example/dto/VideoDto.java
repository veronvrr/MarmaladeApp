package com.example.dto;

import java.util.List;

public class VideoDto {
    private Long id;
    private String title;
    private String level;
    private String fileName;
    private String thumbnail;
    private String description;
    private String transcript;
    private String shortPreview;
    private List<VideoTaskDto> tasks;
    // getters/setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTranscript() {
        return transcript;
    }

    public void setTranscript(String transcript) {
        this.transcript = transcript;
    }

    public String getShortPreview() {
        return shortPreview;
    }

    public void setShortPreview(String shortPreview) {
        this.shortPreview = shortPreview;
    }

    public List<VideoTaskDto> getTasks() {
        return tasks;
    }

    public void setTasks(List<VideoTaskDto> tasks) {
        this.tasks = tasks;
    }

}