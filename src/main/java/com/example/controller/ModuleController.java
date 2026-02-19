
package com.example.controller;

import com.example.dto.QuestionDto;
import com.example.dto.ReadingTextDto;
import com.example.model.AppUser;
import com.example.service.ModuleService;
import com.example.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collections;
import java.util.List;

@Controller
public class ModuleController {

    private final ModuleService moduleService;
    private final UserService userService;

    public ModuleController(ModuleService moduleService, UserService userService) {
        this.moduleService = moduleService;
        this.userService = userService;
    }

    @GetMapping("/modules/grammar")
    public String grammar(@AuthenticationPrincipal UserDetails principal, Model model) {
        AppUser user = null;
        if (principal != null) {
            user = userService.findByUsername(principal.getUsername());
        }
        model.addAttribute("user", user);
        List<QuestionDto> questions = moduleService.getGrammarQuestionsForUser(user);
        if (questions == null) questions = Collections.emptyList();
        model.addAttribute("questions", questions);
        return "modules/grammar";
    }
    // список текстов
    @GetMapping("/modules/reading")
    public String readingList(@AuthenticationPrincipal UserDetails principal, Model model) {
        AppUser user = principal != null ? userService.findByUsername(principal.getUsername()) : null;
        String level = (user != null && user.getLevel() != null) ? user.getLevel() : "A1";
        List<ReadingTextDto> texts = moduleService.getTextsForLevel(level);
        model.addAttribute("user", user);
        model.addAttribute("texts", texts);
        model.addAttribute("text", null);
        return "modules/reading";
    }

    // просмотр одного текста + задач
    @GetMapping("/modules/reading/{id}")
    public String readingView(@PathVariable("id") Long id,
                              @AuthenticationPrincipal UserDetails principal,
                              Model model) {
        AppUser user = principal != null ? userService.findByUsername(principal.getUsername()) : null;
        ReadingTextDto text = moduleService.getTextWithTasks(id);
        if (text == null) return "redirect:/modules/reading";
        String level = (user != null && user.getLevel() != null) ? user.getLevel() : "A1";
        model.addAttribute("user", user);
        model.addAttribute("texts", moduleService.getTextsForLevel(level));
        model.addAttribute("text", text);
        return "modules/reading";
    }

}

