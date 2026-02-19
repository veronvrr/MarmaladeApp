
package com.example.controller;

import com.example.model.LessonRequest;
import com.example.service.LessonRequestService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/requests")
@PreAuthorize("hasRole('ADMIN')")
public class AdminRequestsController {

    private final LessonRequestService requestService;

    public AdminRequestsController(LessonRequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping
    public String list(@RequestParam(name="teacherId", required=false) Long teacherId, Model model) {
        List<LessonRequest> list;
        if (teacherId != null) list = requestService.findByTeacherId(teacherId);
        else list = requestService.findAllSorted();
        model.addAttribute("requests", list);
        return "admin/requests-list";
    }

    @PostMapping("/{id}/approve")
    public String approve(@PathVariable Long id) {
        requestService.setStatus(id, "APPROVED");
        return "redirect:/admin/requests";
    }

    @PostMapping("/{id}/reject")
    public String reject(@PathVariable Long id) {
        requestService.setStatus(id, "REJECTED");
        return "redirect:/admin/requests";
    }
}
