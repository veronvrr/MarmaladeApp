package com.example.controller;

import com.example.model.LessonRequest;
import com.example.service.LessonRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/tutors")
public class TutorApiController {

    private final LessonRequestService requestService;

    public TutorApiController(LessonRequestService requestService) {
        this.requestService = requestService;
    }

    public static class RequestPayload {
        private String studentName;
        private String studentEmail;
        public String getStudentName() { return studentName; }
        public void setStudentName(String studentName) { this.studentName = studentName; }
        public String getStudentEmail() { return studentEmail; }
        public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }
    }

//    @PostMapping("/{id}/request")
//    public ResponseEntity<?> createRequest(@PathVariable("id") Long teacherId,
//                                           @RequestBody RequestPayload payload) {
//        if (payload == null || payload.getStudentName() == null || payload.getStudentEmail() == null) {
//            return ResponseEntity.badRequest().body(Map.of("ok", false, "error", "missing fields"));
//        }
//        LessonRequest saved = requestService.createRequest(teacherId, payload.getStudentName().trim(), payload.getStudentEmail().trim());
//        if (saved == null) {
//            return ResponseEntity.status(404).body(Map.of("ok", false, "error", "teacher not found"));
//        }
//        return ResponseEntity.ok(Map.of("ok", true, "id", saved.getId()));
//    }
    @PostMapping(path="/{id}/request/json", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> submitAjax(@PathVariable Long id,
                                        @AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails principal,
                                        @RequestBody Map<String, String> payload) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "not_authenticated"));
        }
        String username = principal.getUsername();
        String displayName = payload.getOrDefault("studentName", null);
        var saved = requestService.createRequest(id, username, displayName);
        if (saved == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "teacher_not_found"));
        }
        return ResponseEntity.ok(Map.of("ok", true, "id", saved.getId()));
    }

}
