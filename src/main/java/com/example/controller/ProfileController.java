//
//package com.example.controller;
//
//import com.example.model.AppUser;
//import com.example.observer.LogNotification;
//import com.example.observer.NotificationManager;
//import com.example.observer.UiNotification;
//import com.example.repository.AppUserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//
//
//@Controller
//public class ProfileController {
//
//    private final AppUserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    @Autowired
//    public ProfileController(AppUserRepository userRepository, PasswordEncoder passwordEncoder) {
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    // helper — получить AppUser текущего пользователя (или null)
//    private AppUser getCurrentAppUser() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth == null) return null;
//        String username = auth.getName();
//        if (username == null) return null;
//        return userRepository.findByUsername(username);
//    }
//
//    @GetMapping("/profile")
//    public String profileView(Model model) {
//        AppUser user = getCurrentAppUser();
//        if (user == null) {
//            // если не аутентифицирован — редирект на логин
//            return "redirect:/login";
//        }
//        model.addAttribute("user", user);
//        // Для совместимости с дашбордом: активная вкладка profile
//        model.addAttribute("activeTab", "profile");
//        return "profile"; // profile.html — отдельный шаблон или фрагмент
//    }
//
//    @GetMapping("/profile/edit")
//    public String profileEditForm(Model model) {
//        AppUser user = getCurrentAppUser();
//        if (user == null) return "redirect:/login";
//
//        model.addAttribute("user", user);
//        // возможно нужно список уровней/языков для селектов
//        model.addAttribute("levels", new String[] {"A1","A2","B1","B2","C1"});
//        model.addAttribute("languages", new String[] {"Английский"});
//        return "profile_edit"; // profile_edit.html
//    }
//
//@PostMapping("/profile")
//public String profileSave(@RequestParam(name="fullName", required=false) String fullName,
//                          @RequestParam(name="preferredLanguage", required=false) String preferredLanguage,
//                          @RequestParam(name="level", required=false) String level,
//                          @RequestParam(name="password", required=false) String password,
//                          @RequestParam(name="avatar", required=false) MultipartFile avatar,
//                          RedirectAttributes ra) {
//    AppUser user = getCurrentAppUser();
//    if (user == null) return "redirect:/login";
//
//    user.setFullName((fullName == null || fullName.isBlank()) ? user.getFullName() : fullName);
//    user.setPreferredLanguage((preferredLanguage == null || preferredLanguage.isBlank()) ? user.getPreferredLanguage() : preferredLanguage);
//    user.setLevel((level == null || level.isBlank()) ? user.getLevel() : level);
//    if (avatar != null && !avatar.isEmpty()) {
//        try {
//            Path uploadDir = Paths.get("src/main/resources/static/images/avatars");
//            Files.createDirectories(uploadDir);
//
//            String filename = user.getUsername() + "_" + avatar.getOriginalFilename();
//            Path filePath = uploadDir.resolve(filename);
//            avatar.transferTo(filePath.toFile());
//
//            user.setAvatarUrl("/images/avatars/" + filename);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    userRepository.save(user);
//
//    // === Observer pattern usage ===
//    NotificationManager manager = new NotificationManager();
//    manager.addObserver(new UiNotification());
//    manager.addObserver(new LogNotification());
//    manager.notifyObservers("Изменения профиля сохранены!");
//    ra.addFlashAttribute("toast", "Изменения профиля сохранены!");
//    return "redirect:/dashboard?tab=profile";
//
//
//}
//
//}
package com.example.controller;

import com.example.model.AppUser;
import com.example.observer.LogNotification;
import com.example.observer.NotificationManager;
import com.example.observer.UiNotification;
import com.example.repository.AppUserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfileController {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfileController(AppUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // показ формы редактирования
    @GetMapping("/profile/edit")
    public String editProfile(@AuthenticationPrincipal User principal, Model model) {
        String username = principal.getUsername();
        AppUser user = userRepository.findByUsername(username);
        if (user == null) return "redirect:/login";
        model.addAttribute("user", user);
        return "profile_edit";
    }

    // обработка форму
    @PostMapping("/profile/edit")
    public String saveProfile(@AuthenticationPrincipal User principal,
                              @ModelAttribute("user") AppUser formUser,
                              @RequestParam(name="password", required=false) String newPassword,
                              RedirectAttributes ra,
                              Model model) {
        String username = principal.getUsername();
        AppUser user = userRepository.findByUsername(username);
        if (user == null) return "redirect:/login";

        // обновляем fullname, язык, уровень
        user.setFullName(formUser.getFullName());
        user.setPreferredLanguage(formUser.getPreferredLanguage());
        user.setLevel(formUser.getLevel());

        // если в поле пароль что-то есть — хешируем и меняем
        if (newPassword != null && !newPassword.isBlank()) {
            user.setPassword(passwordEncoder.encode(newPassword));
        }

        userRepository.save(user);

//        model.addAttribute("user", user);
//        model.addAttribute("msg", "Профиль успешно сохранён");
//        // остаёмся на странице редактирования (можно редирект на /dashboard?tab=profile)
//        return "profile_edit";
        // === Observer pattern usage ===
    NotificationManager manager = new NotificationManager();
    manager.addObserver(new UiNotification());
    manager.addObserver(new LogNotification());
    manager.notifyObservers("Изменения профиля сохранены!");
    ra.addFlashAttribute("toast", "Изменения профиля сохранены!");
    return "redirect:/dashboard?tab=profile";
    }
}

