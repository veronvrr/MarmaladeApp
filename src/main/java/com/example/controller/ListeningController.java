package com.example.controller;

import com.example.dto.AudioDto;
import com.example.model.AppUser;
import com.example.service.ListeningService;
import com.example.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ListeningController {

    private final ListeningService listeningService;
    private final UserService userService;

    public ListeningController(ListeningService listeningService, UserService userService) {
        this.listeningService = listeningService;
        this.userService = userService;
    }

    @GetMapping("/modules/listening")
    public String list(@AuthenticationPrincipal UserDetails principal, Model model) {
        AppUser user = principal != null ? userService.findByUsername(principal.getUsername()) : null;
        String level = (user != null && user.getLevel() != null) ? user.getLevel() : "A1";
        List<AudioDto> audios = listeningService.getAudiosForLevel(level);
        model.addAttribute("user", user);
        model.addAttribute("audios", audios);
        model.addAttribute("audio", null);
        return "modules/listening";
    }

    @GetMapping("/modules/listening/{id}")
    public String view(@PathVariable Long id, @AuthenticationPrincipal UserDetails principal, Model model) {
        AppUser user = principal != null ? userService.findByUsername(principal.getUsername()) : null;
        AudioDto audio = listeningService.getAudioWithTasks(id);
        if (audio == null) return "redirect:/modules/listening";
        String level = (user != null && user.getLevel() != null) ? user.getLevel() : "A1";
        model.addAttribute("user", user);
        model.addAttribute("audios", listeningService.getAudiosForLevel(level));
        model.addAttribute("audio", audio);
        return "modules/listening";
    }
}
