package com.example.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "audios")
public class Audio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 8)
    private String level; // A1, A2, B1, B2, C1...

    @Column(name = "file_name")
    private String fileName; // файл в /static/audio/

    @Lob
    @Column(columnDefinition = "TEXT")
    private String transcript; // транскрипт (plain / HTML)

    @OneToMany(mappedBy = "audio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AudioTask> tasks;

    // --- getters / setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public String getTranscript() { return transcript; }
    public void setTranscript(String transcript) { this.transcript = transcript; }
    public List<AudioTask> getTasks() { return tasks; }
    public void setTasks(List<AudioTask> tasks) { this.tasks = tasks; }
}
