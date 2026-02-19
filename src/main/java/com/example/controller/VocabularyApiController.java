package com.example.controller;

import com.example.dto.CheckResult;
import com.example.dto.SubmittedAnswer;
import com.example.service.VocabularyService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modules/vocabulary")
public class VocabularyApiController {

    private final VocabularyService vocabularyService;

    public VocabularyApiController(VocabularyService vocabularyService) {
        this.vocabularyService = vocabularyService;
    }

    // Request payload: { "videoId": 12, "answers": [{"questionId":"34","answer":"..."}] }
    public static class CheckRequest {
        private Long videoId;
        private List<SubmittedAnswer> answers;
        public Long getVideoId() { return videoId; }
        public void setVideoId(Long videoId) { this.videoId = videoId; }
        public List<SubmittedAnswer> getAnswers() { return answers; }
        public void setAnswers(List<SubmittedAnswer> answers) { this.answers = answers; }
    }

    @PostMapping("/check")
    public ResponseEntity<CheckResult> check(@RequestBody CheckRequest req,
                                             @AuthenticationPrincipal UserDetails principal) {
        // TODO: при желании можно сохранять результат (user + videoId) в БД
        CheckResult res = vocabularyService.checkSubmittedAnswers(req.getVideoId(), req.getAnswers());
        return ResponseEntity.ok(res);
    }
}
