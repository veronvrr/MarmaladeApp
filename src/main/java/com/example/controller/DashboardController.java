
package com.example.controller;

import com.example.model.AppUser;
import com.example.model.Teacher;
import com.example.repository.AppUserRepository;
import com.example.service.TeacherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Map;

@Controller
public class DashboardController {

    private final AppUserRepository userRepository;
    private final TeacherService teacherService;

    public DashboardController(AppUserRepository userRepository, TeacherService teacherService) {
        this.userRepository = userRepository;
        this.teacherService = teacherService;
    }

    //  получить AppUser текущего пользователя
    private AppUser getCurrentAppUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return null;
        String username = auth.getName();
        if (username == null) return null;
        return userRepository.findByUsername(username);
    }

    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(name = "tab", defaultValue = "learning") String tab,
                            @RequestParam(name = "sort", required = false) String sort,
                            @RequestParam(name = "module", required = false) String module,
                            Model model) {

        
        AppUser currentUser = getCurrentAppUser();
        model.addAttribute("user", currentUser);

        boolean isAdmin = currentUser != null && "ADMIN".equalsIgnoreCase(currentUser.getRole());
        model.addAttribute("isAdmin", isAdmin);

        // активная вкладка (learning | tutors | profile)
        model.addAttribute("activeTab", tab);
        model.addAttribute("selectedModule", module);

        // Заглушки — список доступных учебных модулей
        List<Map<String, String>> modules = List.of(
                Map.of("id", "grammar", "title", "Грамматика", "desc", "Упражнения и объяснения"),
                Map.of("id", "listening", "title", "Аудирование", "desc", "Подкасты и истории"),
                Map.of("id", "reading", "title", "Чтение", "desc", "Книги и тексты"),
                Map.of("id", "vocabulary", "title", "Лексика", "desc", "Видео и задания")
        );
        model.addAttribute("modules", modules);


        //Загрузка реальных репетиторов из БД
        List<Teacher> allTutors = teacherService.findAll(); 
        List<Teacher> preview = allTutors.stream().limit(3).toList();
        model.addAttribute("tutorsPreview", preview);

        return "dashboard"; 
    }
}
