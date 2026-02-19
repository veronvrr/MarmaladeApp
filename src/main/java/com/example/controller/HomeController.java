package com.example.controller;

import com.example.model.Teacher;
import com.example.service.TeacherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {

    private final TeacherService teacherService;

    public HomeController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

//    @GetMapping("/")
//    public String home(Model model) {
//        List<Teacher> teachers = teacherService.getAll();
//        model.addAttribute("teachers", teachers);
//        return "index"; // renders index.ftl
//    }
    @GetMapping("/")
    public String home(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("username", principal.getName());
        }
        return "index";
    }

}
