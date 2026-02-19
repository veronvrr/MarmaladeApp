package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "listening_attempts")
public class ListeningAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // кто сделал попытку (может быть null для анонимных)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private AppUser user;

    @Column(name = "audio_id")
    private Long audioId;

    private int total;
    private int correct;

    @Column(name = "details", length = 4000)
    private String detailsJson; // JSON с per-question результатами

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // getters / setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public AppUser getUser() { return user; }
    public void setUser(AppUser user) { this.user = user; }
    public Long getAudioId() { return audioId; }
    public void setAudioId(Long audioId) { this.audioId = audioId; }
    public int getTotal() { return total; }
    public void setTotal(int total) { this.total = total; }
    public int getCorrect() { return correct; }
    public void setCorrect(int correct) { this.correct = correct; }
    public String getDetailsJson() { return detailsJson; }
    public void setDetailsJson(String detailsJson) { this.detailsJson = detailsJson; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
