//// src/main/java/com/example/service/ModuleService.java
//package com.example.service;
//
//import com.example.model.Question;
//import org.springframework.stereotype.Service;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.UUID;
//
//@Service
//public class ModuleService {
//
//    public List<Question> getGrammarQuestions() {
//        return List.of(
//                new Question("q1",
//                        "I ____ to the store yesterday.",
//                        Question.Type.FILL_IN,
//                        null,
//                        "went"
//                ),
//                new Question("q2",
//                        "Choose the correct phrasal verb: She ____ up on the offer.",
//                        Question.Type.MULTIPLE_CHOICE,
//                        Arrays.asList("gave", "gave up", "gave in", "gave out"),
//                        "gave up"
//                ),
//                new Question("q3",
//                        "Complete the sentence: He ____ (to be) here since Monday.",
//                        Question.Type.FILL_IN,
//                        null,
//                        "has been"
//                )
//        );
//    }
//
//    // Метод проверки: сравнивает ответы, возвращает score + per-question correctness
//    public CheckResult checkGrammarAnswers(List<SubmittedAnswer> submitted) {
//        List<Question> questions = getGrammarQuestions();
//        // Простая реализация: map id->correct answer
//        var map = questions.stream().collect(java.util.stream.Collectors.toMap(Question::getId, Question::getAnswer));
//        int correct = 0;
//        java.util.Map<String, Boolean> per = new java.util.HashMap<>();
//        for (SubmittedAnswer s : submitted) {
//            String expected = map.get(s.getQuestionId());
//            boolean ok = expected != null && expected.trim().equalsIgnoreCase(s.getAnswer().trim());
//            per.put(s.getQuestionId(), ok);
//            if (ok) correct++;
//        }
//        return new CheckResult(correct, submitted.size(), per);
//    }
//
//    // DTOs для проверки
//    public static class SubmittedAnswer {
//        private String questionId;
//        private String answer;
//        // getters/setters, ctor
//        public SubmittedAnswer(){}
//        public SubmittedAnswer(String questionId, String answer){this.questionId=questionId; this.answer=answer;}
//        public String getQuestionId(){return questionId;}
//        public void setQuestionId(String questionId){this.questionId=questionId;}
//        public String getAnswer(){return answer;}
//        public void setAnswer(String answer){this.answer=answer;}
//    }
//    public static class CheckResult {
//        private int correct;
//        private int total;
//        private java.util.Map<String, Boolean> perQuestion;
//        public CheckResult(int correct, int total, java.util.Map<String, Boolean> perQuestion){
//            this.correct=correct;this.total=total;this.perQuestion=perQuestion;
//        }
//        // getters
//        public int getCorrect(){return correct;}
//        public int getTotal(){return total;}
//        public java.util.Map<String, Boolean> getPerQuestion(){return perQuestion;}
//    }
//}

//
//package com.example.service;
//
//import com.example.dto.Question;
//import com.example.dto.SubmittedAnswer;
//import com.example.dto.CheckResult;
//import com.example.model.AppUser;
//import org.springframework.stereotype.Service;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Service
//public class ModuleService {
//
//    // В памяти — демонстрационный набор вопросов
//    private final List<Question> allGrammarQuestions = new ArrayList<>();
//
//    public ModuleService() {
//        seedQuestions();
//    }
//
//    private void seedQuestions() {
//        // A1 — простые fill-in или radio
//        allGrammarQuestions.add(new Question(
//                "g-a1-1",
//                "A1",
//                "Complete: She ___ (to be) a teacher.",
//                null, // options null => текстовое поле
//                "is"
//        ));
//
//        allGrammarQuestions.add(new Question(
//                "g-a1-2",
//                "A1",
//                "Choose correct: I ___ coffee every morning.",
//                Arrays.asList("drink", "drinks", "am drinking"),
//                "drink"
//        ));
//
//        // A2
//        allGrammarQuestions.add(new Question(
//                "g-a2-1",
//                "A2",
//                "Complete: Yesterday they ___ (go) to the cinema.",
//                null,
//                "went"
//        ));
//
//        allGrammarQuestions.add(new Question(
//                "g-a2-2",
//                "A2",
//                "Choose correct: I think it ___ tomorrow.",
//                Arrays.asList("will rain", "rains", "is raining"),
//                "will rain"
//        ));
//
//        // B1
//        allGrammarQuestions.add(new Question(
//                "g-b1-1",
//                "B1",
//                "Complete: If I ___ (have) more time, I would travel.",
//                null,
//                "had"
//        ));
//
//        // Общие (не зависят от уровня) — level == null
//        allGrammarQuestions.add(new Question(
//                "g-common-1",
//                null,
//                "Translate: 'Hello'.",
//                null,
//                "hello"
//        ));
//    }
//
//    /** Возвращает все вопросы (возможно пригодится) */
//    public List<Question> getGrammarQuestions() {
//        return Collections.unmodifiableList(allGrammarQuestions);
//    }
//
//    /**
//     * Возвращает вопросы, отфильтрованные по уровню пользователя.
//     * Если user == null или user.getLevel() == null -> возвращает базовый набор (A1 + common).
//     * Если для уровня нет вопросов — возвращает вопросы с level == null (общие).
//     */
//    public List<Question> getGrammarQuestionsForUser(AppUser user) {
//        String level = (user != null ? user.getLevel() : null);
//        if (level == null || level.isBlank()) {
//            // возвращаем A1 + общие
//            return allGrammarQuestions.stream()
//                    .filter(q -> q.getLevel() == null || "A1".equals(q.getLevel()))
//                    .collect(Collectors.toList());
//        }
//        // возвращаем вопросы с совпадающим уровнем и общие
//        final String userLevel = level.trim().toUpperCase();
//        List<Question> result = allGrammarQuestions.stream()
//                .filter(q -> q.getLevel() == null || userLevel.equalsIgnoreCase(q.getLevel()))
//                .collect(Collectors.toList());
//        if (result.isEmpty()) {
//            // fallback: вернуть общие
//            return allGrammarQuestions.stream().filter(q -> q.getLevel() == null).collect(Collectors.toList());
//        }
//        return result;
//    }
//
//    /**
//     * Серверная проверка: принимает список SubmittedAnswer (questionId, answerValue).
//     * Возвращает CheckResult с подсчётом и perQuestion map.
//     * Сравнение нечувствительно к регистру, разрешает варианты через '|' в эталонном answer.
//     */
//    public CheckResult checkGrammarAnswers(List<SubmittedAnswer> submitted) {
//        Map<String, Question> byId = allGrammarQuestions.stream()
//                .collect(Collectors.toMap(Question::getId, q -> q));
//
//        int total = 0;
//        int correct = 0;
//        Map<String, Boolean> perQuestion = new HashMap<>();
//
//        for (SubmittedAnswer s : submitted) {
//            String qid = s.getQuestionId();
//            String userAns = s.getAnswer() == null ? "" : s.getAnswer().trim();
//            Question q = byId.get(qid);
//            if (q == null) {
//                // неизвестный вопрос — пропускаем
//                continue;
//            }
//            String expected = q.getAnswer() == null ? "" : q.getAnswer().trim();
//            // если нет эталона — считаем как не проверяемый
//            if (expected.isEmpty()) {
//                perQuestion.put(qid, null);
//                continue;
//            }
//            total++;
//            boolean ok = compareAnswers(expected, userAns);
//            if (ok) correct++;
//            perQuestion.put(qid, ok);
//        }
//
//        return new CheckResult(correct, total, perQuestion);
//    }
//
//    private boolean compareAnswers(String expected, String userAns) {
//        // expected может быть "is|'s" или "went" или "hello|hi"
//        String[] variants = expected.split("\\|");
//        String normUser = normalize(userAns);
//        for (String v : variants) {
//            if (normalize(v).equals(normUser)) return true;
//        }
//        return false;
//    }
//
//    private String normalize(String s) {
//        if (s == null) return "";
//        return s.toLowerCase().replaceAll("[\\p{Punct}\\s]+", "").trim();
//    }
//}
//package com.example.service;

//import com.example.dto.CheckResult;
//import com.example.dto.QuestionDto;
//import com.example.dto.SubmittedAnswer;
//import com.example.model.AppUser;
//import org.springframework.stereotype.Service;
//
//import java.text.Normalizer;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Service
//public class ModuleService {
//
//    private final List<QuestionDto> allGrammarQuestions = new ArrayList<>();
//
//    public ModuleService() {
//        seedQuestions();
//    }
//
//    private void seedQuestions() {
//        // A1 examples
//        allGrammarQuestions.add(new QuestionDto("g-a1-1","A1","Complete: She ___ (to be) a teacher.", null, "is"));
//        allGrammarQuestions.add(new Question("g-a1-2","A1","Choose: I ___ coffee every morning.", Arrays.asList("drink","drinks","am drinking"), "drink"));
//
//        // A2 examples
//        allGrammarQuestions.add(new QuestionDt0("g-a2-1","A2","Complete: Yesterday they ___ (go) to the cinema.", null, "went"));
//        allGrammarQuestions.add(new Question("g-a2-2","A2","Choose: I think it ___ tomorrow.", Arrays.asList("will rain","rains","is raining"), "will rain"));
//
//        // B1 example
//        allGrammarQuestions.add(new Question("g-b1-1","B1","Complete: If I ___ (have) more time, I would travel.", null, "had"));
//
//        // common
//        allGrammarQuestions.add(new Question("g-common-1", null, "Translate: 'Hello'.", null, "Привет"));
//    }
//
//    public List<Question> getGrammarQuestions() {
//        return Collections.unmodifiableList(allGrammarQuestions);
//    }
//
//    public List<Question> getGrammarQuestionsForUser(AppUser user) {
//        String level = user != null ? user.getLevel() : null;
//        if (level == null || level.isBlank()) {
//            // базовый набор: A1 + общие
//            return allGrammarQuestions.stream()
//                    .filter(q -> q.getLevel() == null || "A1".equalsIgnoreCase(q.getLevel()))
//                    .collect(Collectors.toList());
//        }
//        final String userLevel = level.trim().toUpperCase();
//        List<Question> result = allGrammarQuestions.stream()
//                .filter(q -> q.getLevel() == null || userLevel.equalsIgnoreCase(q.getLevel()))
//                .collect(Collectors.toList());
//        if (result.isEmpty()) {
//            return allGrammarQuestions.stream().filter(q -> q.getLevel() == null).collect(Collectors.toList());
//        }
//        return result;
//    }
//
//    public CheckResult checkGrammarAnswers(List<SubmittedAnswer> submitted) {
//        Map<String, Question> byId = allGrammarQuestions.stream()
//                .collect(Collectors.toMap(Question::getId, q -> q));
//
//        int total = 0;
//        int correct = 0;
//        Map<String, Boolean> perQuestion = new HashMap<>();
//
//        for (SubmittedAnswer s : submitted) {
//            Question q = byId.get(s.getQuestionId());
//            if (q == null) continue;
//            String expected = q.getAnswer() == null ? "" : q.getAnswer().trim();
//            if (expected.isEmpty()) {
//                perQuestion.put(s.getQuestionId(), null);
//                continue;
//            }
//            total++;
//            boolean ok = compareAnswers(expected, s.getAnswer());
//            if (ok) correct++;
//            perQuestion.put(s.getQuestionId(), ok);
//        }
//
//        return new CheckResult(correct, total, perQuestion);
//    }
//
//    private boolean compareAnswers(String expected, String userAns) {
//        if (userAns == null) userAns = "";
//        String[] variants = expected.split("\\|");
//        String normUser = normalizeJava(userAns);
//        for (String v : variants) {
//            if (normalizeJava(v).equals(normUser)) return true;
//        }
//        return false;
//    }
//

//
//
//    private String normalizeJava(String s) {
//        if (s == null) return "";
//        String n = Normalizer.normalize(s, Normalizer.Form.NFD)
//                .replaceAll("\\p{M}", ""); // удаляет combining marks
//        n = n.toLowerCase(Locale.ROOT);
//        n = n.replaceAll("[^\\p{IsAlphabetic}\\p{IsDigit}]+", ""); // оставить буквы/цифры
//        return n.trim();
//    }
//
//}
package com.example.service;

import com.example.dto.*;
import com.example.model.AppUser;
import com.example.model.QuestionEntity;
import com.example.model.Text;
import com.example.model.TextTask;
import com.example.repository.QuestionRepository;
import com.example.repository.TextRepository;
import com.example.repository.TextTaskRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ModuleService {

    private final QuestionRepository questionRepository;
    private final TextRepository textRepository;
    private final TextTaskRepository textTaskRepository;

    public ModuleService(QuestionRepository questionRepository, TextRepository textRepository, TextTaskRepository taskRepository, TextTaskRepository textTaskRepository) {
        this.questionRepository = questionRepository;
        this.textRepository = textRepository;
        this.textTaskRepository = textTaskRepository;
    }

    // уровень можно получить из user, если user==null -> A1
    public List<QuestionDto> getGrammarQuestionsForUser(AppUser user) {
//        String level = (user != null && user.getLevel() != null) ? user.getLevel() : "A1";
        String level = (user != null && user.getLevel() != null) ? user.getLevel().trim().toUpperCase() : "A1";

        // Попробуем взять из БД
        List<QuestionEntity> list = questionRepository.findByLevel(level);
        if (list != null && !list.isEmpty()) {
            return list.stream().map(this::toDto).collect(Collectors.toList());
        }

        // если в БД пусто — fallback: in-memory
        return getFallbackQuestions(level);
    }

    private QuestionDto toDto(QuestionEntity e) {
        List<String> opts = null;
        if (e.getOptions() != null && !e.getOptions().isBlank()) {
            // split by comma, trim
            opts = Arrays.stream(e.getOptions().split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
        }
        // create a deterministic id
//        String id = (e.getId() != null) ? ("q" + e.getId()) : UUID.randomUUID().toString();
        String id;
        if (e.getId() != null) {
            id = "q" + e.getId();
        } else {
            id = "q" + UUID.randomUUID().toString().replaceAll("[^a-zA-Z0-9\\-]", "");
        }
        return new QuestionDto(id, e.getPrompt(), opts, e.getAnswer());
    }

    // --- fallback in-memory data (если БД пустая) ---
    private List<QuestionDto> getFallbackQuestions(String level) {
        switch (level) {
            case "A1": return a1Questions();
            case "A2": return a2Questions();
            case "B1": return b1Questions();
            case "B2": return b2Questions();
            case "C1": return c1Questions();
            default: return a1Questions();
        }
    }

    private List<QuestionDto> a1Questions() {
        return List.of(
                new QuestionDto("a1-1", "Переведи: 'Hello' →", null, "привет|hello"),
                new QuestionDto("a1-2", "Complete: I ___ (be) a student.", null, "am"),
                new QuestionDto("a1-3", "Выбери верный вариант:", List.of("He go","He goes","He going"), "He goes")
        );
    }

    private List<QuestionDto> a2Questions() {
        return List.of(
                new QuestionDto("a2-1", "Complete: I ___ (visit) London last year.", null, "visited"),
                new QuestionDto("a2-2", "Choose the correct:", List.of("is raining","rains","rain"), "is raining")
        );
    }

    private List<QuestionDto> b1Questions() {
        return List.of(
                new QuestionDto("b1-1", "If I ___ (win) the lottery, I would travel.", null, "won"),
                new QuestionDto("b1-2", "Choose phrasal verb meaning 'postpone':", List.of("put off","give up","take off"), "put off"),
                new QuestionDto("b1-3", "Rewrite passive: 'They built a house.' →", null, "A house was built|was built")
        );
    }

    private List<QuestionDto> b2Questions() {
        return List.of(
                new QuestionDto("b2-1", "Choose correct: 'Had I known, ___ (I / decline) the invitation.'", null, "I would have declined|I'd have declined"),
                new QuestionDto("b2-2", "Complete with a modal: 'You ___ have told me.'", null, "should")
        );
    }
    private List<QuestionDto> c1Questions() {
        return List.of(
                new QuestionDto("c1-1",
                        "Rewrite using inversion: \"Had I known, I would have acted differently.\" — перепиши (вариант):",
                        null,
                        "Had I known, I would have acted differently|Had I known I'd have acted differently"),
                new QuestionDto("c1-2",
                        "Choose the best nuance of meaning for 'to have someone on':",
                        List.of("to employ","to joke with someone","to have someone at your place"),
                        "to joke with someone"),
                new QuestionDto("c1-3",
                        "Fill gap: He ___ (to anticipate) difficulties, so he was prepared.",
                        null,
                        "had anticipated|had anticipated"),
                // можно добавить ещё вопросы
                new QuestionDto("c1-4",
                        "Advanced: Choose correct phrase: 'It would be better ___ he had known earlier.'",
                        List.of("if","had","were"),
                        "if")
        );
    }


    public CheckResult checkSubmittedAnswers(List<SubmittedAnswer> answers) {
        // Загружаем все вопросы в карту по id-ключу в формате, который используется в DTO (q<id>)
        Map<String, QuestionEntity> byId = questionRepository.findAll()
                .stream()
                .collect(Collectors.toMap(e -> "q" + e.getId(), e -> e));

        int total = 0;
        int correct = 0;
        Map<String, Boolean> perQuestion = new LinkedHashMap<>();

        for (SubmittedAnswer sa : answers) {
            total++;
            QuestionEntity q = byId.get(sa.getQuestionId());
            boolean ok = false;
            if (q != null && q.getAnswer() != null) {
                String expected = q.getAnswer();
                List<String> allowed = Arrays.stream(expected.split("\\|"))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .map(String::toLowerCase)
                        .collect(Collectors.toList());
                String candidate = Optional.ofNullable(sa.getAnswer()).orElse("").trim().toLowerCase();
                ok = allowed.contains(candidate);
            } else {
                ok = false;
            }
            perQuestion.put(sa.getQuestionId(), ok);
            if (ok) correct++;
        }

        return new CheckResult(correct, total, perQuestion);
    }



    // список текстов для уровня
    public List<ReadingTextDto> getTextsForLevel(String level) {
        List<Text> texts = textRepository.findByLevel(level);
        if (texts == null) texts = Collections.emptyList();
        return texts.stream().map(this::toDtoBasic).collect(Collectors.toList());
    }

    // подробный текст + задачи
    public ReadingTextDto getTextWithTasks(Long id) {
        Optional<Text> o = textRepository.findById(id);
        if (o.isEmpty()) return null;
        Text t = o.get();
        ReadingTextDto dto = toDtoBasic(t);

        // получаем задачи отдельным запросом (избегаем проблем lazy loading)
        List<TextTask> tasks = textTaskRepository.findByTextIdOrderByOrderIndex(id);
        List<ReadingTaskDto> taskDtos = tasks.stream().map(this::toTaskDto).collect(Collectors.toList());
        dto.setTasks(taskDtos);
        return dto;
    }

    private ReadingTextDto toDtoBasic(Text t) {
        ReadingTextDto dto = new ReadingTextDto();
        dto.setId(t.getId());
        dto.setTitle(t.getTitle());
        dto.setContent(t.getContent());
        dto.setLevel(t.getLevel());
        dto.setShortPreview(makePreview(t.getContent()));
        return dto;
    }

    private ReadingTaskDto toTaskDto(TextTask t) {
        ReadingTaskDto dto = new ReadingTaskDto();
        dto.setId(t.getId());
        dto.setType(t.getType());
        dto.setPrompt(t.getPrompt());
        // options: парсим по запятой (если null или пусто -> null)
        if (t.getOptions() != null && !t.getOptions().isBlank()) {
            List<String> opts = Arrays.stream(t.getOptions().split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
            dto.setOptions(opts);
        } else {
            dto.setOptions(null);
        }
        dto.setAnswer(t.getAnswer());
        return dto;
    }

    private String makePreview(String content) {
        if (content == null) return "";
        String s = content.replaceAll("\\s+", " ").trim();
        int max = 140;
        return s.length() > max ? s.substring(0, max) + "..." : s;
    }

}
