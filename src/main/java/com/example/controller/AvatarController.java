package com.example.controller;

import com.example.model.AppUser;
import com.example.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Controller
public class AvatarController {

    private final AppUserRepository userRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;

    public AvatarController(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/profile/avatar")
    public String uploadAvatar(@AuthenticationPrincipal User principal,
                               MultipartFile avatar,
                               RedirectAttributes ra) throws IOException {
        if (principal == null) return "redirect:/login";

        if (avatar == null || avatar.isEmpty()) {
            ra.addFlashAttribute("msgError", "Файл не выбран");
            return "redirect:/dashboard?tab=profile";
        }

        // Валидация типа
        String contentType = avatar.getContentType();
        if (contentType == null ||
                !(contentType.equalsIgnoreCase("image/png") ||
                        contentType.equalsIgnoreCase("image/jpeg") ||
                        contentType.equalsIgnoreCase("image/jpg"))) {
            ra.addFlashAttribute("msgError", "Разрешены только PNG и JPG");
            return "redirect:/dashboard?tab=profile";
        }

       
        long maxBytes = 5 * 1024 * 1024L; // 5MB
        if (avatar.getSize() > maxBytes) {
            ra.addFlashAttribute("msgError", "Файл слишком большой (макс 5MB)");
            return "redirect:/dashboard?tab=profile";
        }

       
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        
        String original = StringUtils.cleanPath(avatar.getOriginalFilename());
        String ext = "";
        int idx = original.lastIndexOf('.');
        if (idx > 0) ext = original.substring(idx); // .png или .jpg

        String newFileName = UUID.randomUUID().toString() + ext;

        // Сохраняем на диск
        Path target = uploadPath.resolve(newFileName);
        try {
            Files.copy(avatar.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            ra.addFlashAttribute("msgError", "Ошибка сохранения файла");
            return "redirect:/dashboard?tab=profile";
        }

        // Сохраняем имя файла в БД
        AppUser user = userRepository.findByUsername(principal.getUsername());
        if (user == null) return "redirect:/login";

        // Удалить старый файл
        String old = user.getAvatarFilename();
        if (old != null && !old.isBlank()) {
            try {
                Path oldPath = uploadPath.resolve(old);
                Files.deleteIfExists(oldPath);
            } catch (IOException ignored) {}
        }

        user.setAvatarFilename(newFileName);
        userRepository.save(user);

        ra.addFlashAttribute("msg","Аватар загружен");
        return "redirect:/dashboard?tab=profile";
    }
}
