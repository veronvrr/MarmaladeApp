//package com.example.controller;
//
//import com.example.dto.ReadingTextDto;        // или свой DTO для Vocabulary (можно переиспользовать)
//import com.example.dto.ReadingTaskDto;
//import com.example.model.AppUser;
//import com.example.service.ModuleService;    // или VocabularyService, если есть
//import com.example.service.UserService;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//
//import java.util.List;
//
//@Controller
//public class VocabularyController {
//
//    private final ModuleService moduleService; // если у тебя отдельный VocabularyService — используй его
//    private final UserService userService;
//
//    public VocabularyController(ModuleService moduleService, UserService userService) {
//        this.moduleService = moduleService;
//        this.userService = userService;
//    }
//
//    // список: /modules/vocabulary
//    @GetMapping("/modules/vocabulary")
//    public String list(@AuthenticationPrincipal UserDetails principal, Model model) {
//        AppUser user = principal != null ? userService.findByUsername(principal.getUsername()) : null;
//        String level = (user != null && user.getLevel() != null) ? user.getLevel() : "A1";
//
//        // переиспользуем метод для получения "texts" — если у тебя есть отдельный метод, используй его
//        List<ReadingTextDto> items = moduleService.getTextsForLevel(level); // или moduleService.getVocabularyForLevel(level)
//        model.addAttribute("user", user);
//        model.addAttribute("items", items);
//        return "modules/vocabulary";
//    }
//
//    // просмотр одного: /modules/vocabulary/{id}
//    @GetMapping("/modules/vocabulary/{id}")
//    public String view(@PathVariable("id") Long id,
//                       @AuthenticationPrincipal UserDetails principal,
//                       Model model) {
//        AppUser user = principal != null ? userService.findByUsername(principal.getUsername()) : null;
//
//        // в ModuleService добавь метод getVocabularyWithTasks(id) аналогично чтению
//        ReadingTextDto item = moduleService.getTextWithTasks(id); // переименуй или создай getVocabularyWithTasks
//        if (item == null) {
//            return "redirect:/modules/vocabulary";
//        }
//
//        model.addAttribute("user", user);
//        model.addAttribute("item", item);
//        return "modules/vocabulary";
//    }
//}
package com.example.controller;

import com.example.dto.VideoDto;
import com.example.model.AppUser;
import com.example.service.VocabularyService;
import com.example.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class VocabularyController {

    private final VocabularyService vocabularyService;
    private final UserService userService;

    public VocabularyController(VocabularyService vocabularyService, UserService userService) {
        this.vocabularyService = vocabularyService;
        this.userService = userService;
    }

    // Список видео /modules/vocabulary
    @GetMapping("/modules/vocabulary")
    public String list(@AuthenticationPrincipal UserDetails principal, Model model) {
        AppUser user = principal != null ? userService.findByUsername(principal.getUsername()) : null;
        String level = (user != null && user.getLevel() != null) ? user.getLevel() : "A1";

        List<VideoDto> videos = vocabularyService.getVideosForLevel(level);
        model.addAttribute("user", user);
        model.addAttribute("videos", videos);
        model.addAttribute("video", null); // шаблон показывает список, если video == null
        return "modules/vocabulary";
    }

    // Просмотр конкретного видео + задач
    @GetMapping("/modules/vocabulary/{id}")
    public String view(@PathVariable("id") Long id,
                       @AuthenticationPrincipal UserDetails principal,
                       Model model) {
        AppUser user = principal != null ? userService.findByUsername(principal.getUsername()) : null;

        VideoDto video = vocabularyService.getVideoWithTasks(id);
        if (video == null) {
            return "redirect:/modules/vocabulary";
        }

        String level = (user != null && user.getLevel() != null) ? user.getLevel() : "A1";
        model.addAttribute("user", user);
        model.addAttribute("videos", vocabularyService.getVideosForLevel(level)); // список для превью
        model.addAttribute("video", video);
        return "modules/vocabulary";
    }
}
