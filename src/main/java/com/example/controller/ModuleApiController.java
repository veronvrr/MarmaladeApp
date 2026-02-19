package com.example.controller;

import com.example.dto.CheckResult;
import com.example.dto.SubmittedAnswer;
import com.example.service.ModuleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modules/grammar")
public class ModuleApiController {

    private final ModuleService moduleService;

    public ModuleApiController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    @PostMapping("/check")
    public CheckResult checkAnswers(@RequestBody List<SubmittedAnswer> answers) {
        return moduleService.checkSubmittedAnswers(answers);
    }
}
