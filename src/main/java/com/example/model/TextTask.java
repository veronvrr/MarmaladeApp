package com.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "text_tasks")
public class TextTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Тип задания: "tf" (true/false), "mcq" (multiple choice), "fill" (fill gap), "short" (short answer)
    private String type;

    @Column(length = 2000)
    private String prompt; // вопрос / инструкция

    // Для mcq: варианты через '||' или ',' — будем использовать запятую
    @Column(length = 2000)
    private String options; // nullable

    // правильный ответ (для multiple — точный текст одного варианта; для текстовых — варианты через '|')
    @Column(length = 2000)
    private String answer;

    // порядок в тексте
    private Integer orderIndex = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "text_id")
    private Text text;

    // getters / setters
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
    public Text getText() { return text; }
    public void setText(Text text) { this.text = text; }
}
