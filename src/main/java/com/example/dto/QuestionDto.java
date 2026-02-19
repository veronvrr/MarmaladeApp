//package com.example.dto;
//
//import java.util.List;
//
///**
// * Простая модель вопроса:
// * id     - уникальный идентификатор строки/вопроса (строка удобнее для HTML name)
// * level  - уровень ("A1","A2"...). Может быть null = общий
// * prompt - текст вопроса (короткий) — используем в шаблоне
// * options - если не-null => многовариантный выбор (radio)
// * answer  - эталонный ответ(ы) для server-side проверки. Варианты через '|' разделитель, без учета регистра.
// */
//public class Question {
//    private String id;
//    private String level;
//    private String prompt;
//    private List<String> options;
//    private String answer;
//
//    public Question() {}
//
//    public Question(String id, String level, String prompt, List<String> options, String answer) {
//        this.id = id;
//        this.level = level;
//        this.prompt = prompt;
//        this.options = options;
//        this.answer = answer;
//    }
//
//    // геттеры/сеттеры
//    public String getId() { return id; }
//    public void setId(String id) { this.id = id; }
//
//    public String getLevel() { return level; }
//    public void setLevel(String level) { this.level = level; }
//
//    public String getPrompt() { return prompt; }
//    public void setPrompt(String prompt) { this.prompt = prompt; }
//
//    public List<String> getOptions() { return options; }
//    public void setOptions(List<String> options) { this.options = options; }
//
//    public String getAnswer() { return answer; }
//    public void setAnswer(String answer) { this.answer = answer; }
//}
//package com.example.dto;
//
//import java.util.List;
//
///**
// * Вопрос/единица упражнений.
// * options == null -> текстовый input
// * options != null -> многовариантный выбор (radio)
// */
//public class Question {
//    private String id;
//    private String level;      // "A1","A2" ... or null
//    private String prompt;     // текст подсказки / вопроса (короткий)
//    private List<String> options;
//    private String answer;     // эталон, варианты через '|' допустимы
//
//    public Question() {}
//
//    public Question(String id, String level, String prompt, List<String> options, String answer) {
//        this.id = id;
//        this.level = level;
//        this.prompt = prompt;
//        this.options = options;
//        this.answer = answer;
//    }
//
//    // геттеры/сеттеры...
//    public String getId() { return id; }
//    public void setId(String id) { this.id = id; }
//    public String getLevel() { return level; }
//    public void setLevel(String level) { this.level = level; }
//    public String getPrompt() { return prompt; }
//    public void setPrompt(String prompt) { this.prompt = prompt; }
//    public List<String> getOptions() { return options; }
//    public void setOptions(List<String> options) { this.options = options; }
//    public String getAnswer() { return answer; }
//    public void setAnswer(String answer) { this.answer = answer; }
//}
package com.example.dto;

import java.util.List;

public class QuestionDto {
    private String id;
    private String prompt;
    private List<String> options; // null -> текстовое поле
    private String answer;

    public QuestionDto() {}

    public QuestionDto(String id, String prompt, List<String> options, String answer) {
        this.id = id;
        this.prompt = prompt;
        this.options = options;
        this.answer = answer;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPrompt() { return prompt; }
    public void setPrompt(String prompt) { this.prompt = prompt; }

    public List<String> getOptions() { return options; }
    public void setOptions(List<String> options) { this.options = options; }

    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }
}
