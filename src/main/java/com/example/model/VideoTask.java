package com.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "video_tasks")
public class VideoTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // types: "mcq", "fill", "match", "tf", "time" (etc.)
    private String type;

    @Column(length = 2000)
    private String prompt;

    @Column(length = 2000)
    private String options; // comma-separated options for mcq, match pairs etc.

    @Column(length = 2000)
    private String answer;  // expected answer(s), '|' separated

    private Integer orderIndex = 0;

    // optional: timestamp in seconds for "time-based" question
    private Integer timecodeSec;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id")
    private Video video;

    // getters/setters ...
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Integer getTimecodeSec() {
        return timecodeSec;
    }

    public void setTimecodeSec(Integer timecodeSec) {
        this.timecodeSec = timecodeSec;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

}
