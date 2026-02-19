package com.example.service;

import com.example.dto.CheckResult;
import com.example.dto.SubmittedAnswer;
import com.example.dto.VideoDto;
import com.example.dto.VideoTaskDto;
import com.example.model.Video;
import com.example.model.VideoTask;
import com.example.repository.VideoRepository;
import com.example.repository.VideoTaskRepository;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VocabularyService {

    private final VideoRepository videoRepository;
    private final VideoTaskRepository videoTaskRepository;

    public VocabularyService(VideoRepository videoRepository, VideoTaskRepository videoTaskRepository) {
        this.videoRepository = videoRepository;
        this.videoTaskRepository = videoTaskRepository;
    }


    public List<VideoDto> getVideosForLevel(String level) {
        if (level == null) level = "A1";
        List<Video> list = videoRepository.findByLevel(level);
        if (list == null) list = Collections.emptyList();
        return list.stream().map(this::toDtoBasic).collect(Collectors.toList());
    }

    public VideoDto getVideoWithTasks(Long id) {
        Optional<Video> opt = videoRepository.findById(id);
        if (opt.isEmpty()) return null;
        Video v = opt.get();
        VideoDto dto = toDtoFull(v);

        // загружаем задачи отдельно (чтобы не полагаться на lazy)
        List<VideoTask> tasks = videoTaskRepository.findByVideoIdOrderByOrderIndex(id);
        List<VideoTaskDto> dtos = tasks.stream().map(this::toTaskDto).collect(Collectors.toList());
        dto.setTasks(dtos);
        return dto;
    }


    private VideoDto toDtoBasic(Video v) {
        VideoDto d = new VideoDto();
        d.setId(v.getId());
        d.setTitle(v.getTitle());
        d.setLevel(v.getLevel());
        d.setFileName(v.getFileName());
        d.setThumbnail(v.getThumbnail());
        d.setDescription(v.getDescription());
        d.setTranscript(v.getTranscript());
        d.setShortPreview(makePreview(v.getDescription() != null ? v.getDescription() : v.getTranscript()));
        return d;
    }

    private VideoDto toDtoFull(Video v) {
        VideoDto d = toDtoBasic(v);
        d.setDescription(v.getDescription());

        return d;
    }

    private VideoTaskDto toTaskDto(VideoTask t) {
        VideoTaskDto dto = new VideoTaskDto();
        dto.setId(t.getId());
        dto.setType(t.getType());
        dto.setPrompt(t.getPrompt());
        dto.setTimecodeSec(t.getTimecodeSec());
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

    private String makePreview(String s) {
        if (s == null) return "";
        String clean = s.replaceAll("\\s+", " ").trim();
        int max = 140;
        return clean.length() > max ? clean.substring(0, max) + "..." : clean;
    }

    // --------------------------
    // Проверка ответов (твоя логика) — возвращает CheckResult с perQuestion map
    // --------------------------
    public CheckResult checkSubmittedAnswers(Long videoId, List<SubmittedAnswer> answers) {
        if (answers == null) answers = Collections.emptyList();

        // Собираем ids присланных вопросов (Long)
        List<Long> ids = answers.stream()
                .map(SubmittedAnswer::getQuestionId)
                .filter(Objects::nonNull)
                .map(idStr -> {
                    try { return Long.valueOf(idStr); } catch (Exception ex) { return null; }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        Map<Long, VideoTask> tasksMap = new HashMap<>();
        if (!ids.isEmpty()) {
            videoTaskRepository.findAllById(ids).forEach(t -> tasksMap.put(t.getId(), t));
        }

        int total = 0;
        int correct = 0;
        Map<String, Boolean> perQuestion = new LinkedHashMap<>();

        for (SubmittedAnswer sa : answers) {
            String qidStr = sa.getQuestionId();
            String userAns = sa.getAnswer() == null ? "" : sa.getAnswer();

            if (qidStr == null) {
                perQuestion.put(null, false);
                continue;
            }

            Long qid;
            try {
                qid = Long.valueOf(qidStr);
            } catch (Exception ex) {
                // некорректный id — отмечаем false
                perQuestion.put(qidStr, false);
                continue;
            }

            VideoTask task = tasksMap.get(qid);
            boolean ok = false;
            if (task != null && task.getAnswer() != null && !task.getAnswer().isBlank()) {
                String expected = task.getAnswer();
                List<String> allowed = Arrays.stream(expected.split("\\|"))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .map(this::normalize)
                        .collect(Collectors.toList());

                String candidate = normalize(userAns);
                ok = allowed.contains(candidate);
            } else {
                // если эталона нет — считаем false (или изменить логику)
                ok = false;
            }

            perQuestion.put(String.valueOf(qid), ok);
            total++;
            if (ok) correct++;
        }

        return new CheckResult(correct, total, perQuestion);
    }


    private String normalize(String s) {
        if (s == null) return "";
        String x = s.trim();
        x = Normalizer.normalize(x, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        x = x.toLowerCase(Locale.ROOT);
        x = x.replaceAll("[^\\p{IsAlphabetic}\\p{IsDigit}\\s]+", "");
        x = x.replaceAll("\\s+", " ").trim();
        return x;
    }
}
