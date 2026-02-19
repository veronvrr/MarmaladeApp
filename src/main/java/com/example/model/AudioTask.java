package com.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "audio_tasks")
public class AudioTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // тип: "tf", "mcq", "fill", "short"
    private String type;

    @Column(length = 2000)
    private String prompt;

    // варианты через запятую (для mcq)
    @Column(length = 2000)
    private String options;

    // правильный ответ (варианты через '|')
    @Column(length = 2000)
    private String answer;

    private Integer orderIndex = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "audio_id")
    private Audio audio;

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getPrompt() { return prompt; }
    public void setPrompt(String prompt) { this.prompt = prompt; }
    public String getOptions() { return options; }
    public void setOptions(String options) { this.options = options; }
    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }
    public Integer getOrderIndex() { return orderIndex; }
    public void setOrderIndex(Integer orderIndex) { this.orderIndex = orderIndex; }
    public Audio getAudio() { return audio; }
    public void setAudio(Audio audio) { this.audio = audio; }
}
