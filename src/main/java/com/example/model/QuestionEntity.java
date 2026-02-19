
package com.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "question")
public class QuestionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // уровень: A1, A2, B1, B2, ...
    @Column(name = "level", length = 8)
    private String level;

    @Column(name = "prompt", length = 2000)
    private String prompt;

    // варианты через запятую; null/empty = текстовый ответ
    @Column(name = "options", length = 2000)
    private String options;

    // правильный ответ(ы) — варианты через '|'
    @Column(name = "answer", length = 2000)
    private String answer;

    public QuestionEntity() {}

    public QuestionEntity(String level, String prompt, String options, String answer) {
        this.level = level;
        this.prompt = prompt;
        this.options = options;
        this.answer = answer;
    }

    // getters / setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    public String getPrompt() { return prompt; }
    public void setPrompt(String prompt) { this.prompt = prompt; }
    public String getOptions() { return options; }
    public void setOptions(String options) { this.options = options; }
    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }
}

