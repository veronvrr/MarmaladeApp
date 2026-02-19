package com.example.controller;

import com.example.dto.ListeningCheckRequest;
import com.example.dto.ListeningCheckResultDto;
import com.example.model.AppUser;
import com.example.service.ListeningService;
import com.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/modules/listening")
public class ListeningApiController {

    private final ListeningService listeningService;
    private final UserService userService;

    public ListeningApiController(ListeningService listeningService, UserService userService) {
        this.listeningService = listeningService;
        this.userService = userService;
    }

    @PostMapping("/check")
    public ResponseEntity<ListeningCheckResultDto> checkAnswers(
            @RequestBody ListeningCheckRequest request,
            @AuthenticationPrincipal UserDetails principal) {

        AppUser user = null;
        if (principal != null) {
            user = userService.findByUsername(principal.getUsername());
        }

        if (request == null || request.getAudioId() == null) {
            return ResponseEntity.badRequest().build();
        }

        ListeningCheckResultDto res = listeningService.checkAndSaveAttempt(
                request.getAudioId(),
                request.getAnswers(),
                user,
                true // persist attempt
        );

        return ResponseEntity.ok(res);
    }
}
