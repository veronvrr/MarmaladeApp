package com.example.service;

import com.example.dto.AudioDto;
import com.example.dto.AudioTaskDto;
import com.example.dto.ListeningCheckResultDto;
import com.example.dto.ListeningSubmittedAnswerDto;
import com.example.model.Audio;
import com.example.model.AudioTask;
import com.example.model.AppUser;
import com.example.model.ListeningAttempt;
import com.example.repository.AudioRepository;
import com.example.repository.AudioTaskRepository;
import com.example.repository.ListeningAttemptRepository;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ListeningService {

    private final AudioRepository audioRepository;
    private final AudioTaskRepository audioTaskRepository;
    private final ListeningAttemptRepository listeningAttemptRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ListeningService(AudioRepository audioRepository,
                            AudioTaskRepository audioTaskRepository,
                            ListeningAttemptRepository listeningAttemptRepository) {
        this.audioRepository = audioRepository;
        this.audioTaskRepository = audioTaskRepository;
        this.listeningAttemptRepository = listeningAttemptRepository;
    }

    // (существующие методы)
    public List<AudioDto> getAudiosForLevel(String level) {
        List<Audio> list = audioRepository.findByLevel(level);
        if (list == null) list = Collections.emptyList();
        return list.stream().map(this::toDtoBasic).collect(Collectors.toList());
    }

    public AudioDto getAudioWithTasks(Long id) {
        Optional<Audio> o = audioRepository.findById(id);
        if (o.isEmpty()) return null;
        Audio a = o.get();
        AudioDto dto = toDtoBasic(a);
        List<AudioTask> tasks = audioTaskRepository.findByAudioIdOrderByOrderIndex(id);
        List<AudioTaskDto> tds = tasks.stream().map(this::toTaskDto).collect(Collectors.toList());
        dto.setTasks(tds);
        return dto;
    }

    // серверная проверка
    /**
     * Проверяет ответы пользователя на сервере и сохраняет попытку.
     * @param audioId id аудио
     * @param submitted список ответов (questionId -> answer)
     * @param user пользователь (может быть null)
     * @param persist если true — сохраняем попытку в БД
     * @return ListeningCheckResultDto с результатом
     */
    public ListeningCheckResultDto checkAndSaveAttempt(Long audioId,
                                                       List<ListeningSubmittedAnswerDto> submitted,
                                                       AppUser user,
                                                       boolean persist) {
        // загрузим задачи аудио
        List<AudioTask> tasks = audioTaskRepository.findByAudioIdOrderByOrderIndex(audioId);
        Map<String, AudioTask> byId = tasks.stream()
                .collect(Collectors.toMap(t -> String.valueOf(t.getId()), t -> t));

        // map submitted by id
        Map<String, String> submittedMap = new HashMap<>();
        if (submitted != null) {
            for (ListeningSubmittedAnswerDto s : submitted) {
                if (s != null && s.getQuestionId() != null) {
                    submittedMap.put(s.getQuestionId(), s.getAnswer() == null ? "" : s.getAnswer().trim());
                }
            }
        }

        Map<String, Boolean> perQuestion = new LinkedHashMap<>();
        int correct = 0;
        int total = tasks.size(); // считаем за общее число задач именно все задачи, даже если не прислали ответ

        for (AudioTask t : tasks) {
            String qid = String.valueOf(t.getId());
            String expected = t.getAnswer() == null ? "" : t.getAnswer().trim();
            String userAns = submittedMap.getOrDefault(qid, "").trim();

            boolean ok = false;
            if (!expected.isEmpty()) {
                List<String> allowed = Arrays.stream(expected.split("\\|"))
                        .map(String::trim).filter(s -> !s.isEmpty())
                        .map(String::toLowerCase).collect(Collectors.toList());
                String candidate = userAns.toLowerCase();
                ok = allowed.contains(candidate);
            }
            perQuestion.put(qid, ok);
            if (ok) correct++;
        }

        ListeningCheckResultDto result = new ListeningCheckResultDto(correct, total, perQuestion);

        if (persist) {
            // сохранение попытки
            ListeningAttempt attempt = new ListeningAttempt();
            attempt.setAudioId(audioId);
            attempt.setUser(user);
            attempt.setTotal(total);
            attempt.setCorrect(correct);
            try {
                String json = objectMapper.writeValueAsString(perQuestion);
                attempt.setDetailsJson(json);
            } catch (Exception ex) {
                attempt.setDetailsJson("{}");
            }
            listeningAttemptRepository.save(attempt);
        }

        return result;
    }

    // вспомогательные конвертеры (как раньше)
    private AudioDto toDtoBasic(Audio a) {
        AudioDto dto = new AudioDto();
        dto.setId(a.getId());
        dto.setTitle(a.getTitle());
        dto.setLevel(a.getLevel());
        dto.setFileName(a.getFileName());
        dto.setTranscript(a.getTranscript());
        dto.setShortPreview(makePreview(a.getTranscript() != null ? a.getTranscript() : a.getTitle()));
        return dto;
    }

    private AudioTaskDto toTaskDto(AudioTask t) {
        AudioTaskDto td = new AudioTaskDto();
        td.setId(t.getId());
        td.setType(t.getType());
        td.setPrompt(t.getPrompt());
        if (t.getOptions() != null && !t.getOptions().isBlank()) {
            List<String> opts = Arrays.stream(t.getOptions().split(","))
                    .map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList());
            td.setOptions(opts);
        } else {
            td.setOptions(null);
        }
        td.setAnswer(t.getAnswer());
        return td;
    }

    private String makePreview(String content) {
        if (content == null) return "";
        String s = content.replaceAll("\\s+", " ").trim();
        int max = 140;
        return s.length() > max ? s.substring(0, max) + "..." : s;
    }
}
