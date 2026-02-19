package com.example.controller;

import com.example.model.Teacher;
import com.example.service.TeacherService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/teachers")
@PreAuthorize("hasRole('ADMIN')")
public class AdminTeacherController {

    private final TeacherService teacherService;

    public AdminTeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }


    @GetMapping("/new")
    public String newTeacherForm(Model model) {
        model.addAttribute("teacher", new Teacher());
        return "admin/teacher-form";
    }

    // обработка сохранения
    @PostMapping("/new")
    public String createTeacher(@ModelAttribute Teacher teacher) {
        teacherService.createTeacher(teacher);
        return "redirect:/teachers";
    }

    @PostMapping("/{id}/delete")
    public String deleteTeacher(@PathVariable Long id) {
            teacherService.deleteTeacher(id);
            return "redirect:/teachers";
    }

}
