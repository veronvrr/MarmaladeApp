package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "texts")
public class Text {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 8)
    private String level; // A1, A2, B1, B2, C1...

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createdAt = LocalDateTime.now();

    // optional bi-directional (not required), comment out if не нужен:
    @OneToMany(mappedBy = "text", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TextTask> tasks;

    // getters / setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public List<TextTask> getTasks() { return tasks; }
    public void setTasks(List<TextTask> tasks) { this.tasks = tasks; }
}
