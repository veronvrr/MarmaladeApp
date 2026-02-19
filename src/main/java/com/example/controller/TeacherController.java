package com.example.controller;

import com.example.model.AppUser;
import com.example.model.LessonRequest;
import com.example.model.Teacher;
import com.example.service.LessonRequestService;
import com.example.service.TeacherService;
import com.example.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherService teacherService;
    private final LessonRequestService requestService;
    private final UserService userService;

    public TeacherController(TeacherService teacherService, LessonRequestService requestService, UserService userService) {
        this.teacherService = teacherService;
        this.requestService = requestService;
        this.userService = userService;
    }



@GetMapping
public String list(@RequestParam(name="sort", required=false) String sort,
                   Model model,
                   @AuthenticationPrincipal UserDetails principal) {
    List<Teacher> teachers = (sort != null && !sort.isBlank()) ? teacherService.findAllSorted(sort) : teacherService.findAll();
    model.addAttribute("teachers", teachers);
    model.addAttribute("currentSort", sort == null ? "" : sort);

    AppUser user = (principal != null) ? userService.findByUsername(principal.getUsername()) : null;
    model.addAttribute("user", user);
    model.addAttribute("isAdmin", user != null && "ADMIN".equalsIgnoreCase(user.getRole()));

    return "teachers/list";
}



    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model,
                         @AuthenticationPrincipal UserDetails principal) {
        Optional<Teacher> tOpt = teacherService.findById(id);
        if (tOpt.isEmpty()) return "redirect:/teachers";
        Teacher t = tOpt.get();
        model.addAttribute("teacher", t);
        if (principal != null) {
            AppUser user = userService.findByUsername(principal.getUsername());
            model.addAttribute("user", user);
            model.addAttribute("isAdmin", user != null && "ADMIN".equalsIgnoreCase(user.getRole()));
        }

        return "teachers/detail";
    }

@PostMapping("/{id}/request")
public String submitForm(@PathVariable Long id,
                         @AuthenticationPrincipal UserDetails principal,
                         @RequestParam(name = "studentName", required = false) String studentName,
                         Model model) {
    if (principal == null) return "redirect:/login";
    String username = principal.getUsername();
    LessonRequest saved = requestService.createRequest(id, username, studentName);
    if (saved == null) {
        model.addAttribute("error", "Преподаватель не найден");
        return "teachers/detail";
    }
    return "redirect:/teachers/" + id + "?sent=1";
}


@PostMapping(value = "/{id}/request", consumes = "application/json")
@ResponseBody
public ResponseEntity<?> submitJson(@PathVariable Long id,
                                    @RequestBody Map<String, String> body,
                                    @AuthenticationPrincipal UserDetails principal) {
    String studentName = body.getOrDefault("studentName", "");
    String studentUsername = body.getOrDefault("studentUsername", "");
    String username = (principal != null) ? principal.getUsername() : studentUsername;
    if (username == null || username.isBlank()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login required");
    }
    var saved = requestService.createRequest(id, studentName, username);
    if (saved == null) return ResponseEntity.badRequest().body("Teacher not found");
    return ResponseEntity.ok(Map.of("ok", true));
}
    @PostMapping("/{id}/edit")
    public String editTeacher(@PathVariable Long id,
                              @ModelAttribute Teacher updated,
                              @AuthenticationPrincipal UserDetails principal) {
        AppUser user = (principal != null) ? userService.findByUsername(principal.getUsername()) : null;
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            return "redirect:/teachers/" + id; // защита: только админ
        }
        teacherService.updateTeacher(id, updated);
        return "redirect:/teachers/" + id + "?edited=1";
    }


}
