//
//package com.example.controller;
//
//import com.example.model.AppUser;
//import com.example.repository.AppUserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//
//import java.util.List;
//
//@Controller
//public class AuthController {
//
//    @Autowired
//    private AppUserRepository userRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @GetMapping("/register")
//    public String registerPage(Model model) {
//        model.addAttribute("userForm", new AppUser());
//        model.addAttribute("languages", List.of("Английский"));
//        model.addAttribute("levels", List.of("A1","A2","B1","B2","C1"));
//        return "register";
//    }
//
//    @PostMapping("/register")
//    public String register(AppUser user, Model model) {
//        // Проверки
//        if (user.getUsername() == null || user.getUsername().isBlank()) {
//            model.addAttribute("error", "Введите логин");
//            model.addAttribute("userForm", user);
//            model.addAttribute("languages", List.of("Английский"));
//            model.addAttribute("levels", List.of("A1","A2","B1","B2","C1"));
//            return "register";
//        }
//
//        AppUser existing = userRepository.findByUsername(user.getUsername());
//        if (existing != null) {
//            model.addAttribute("error", "Такой логин уже существует");
//            model.addAttribute("userForm", user);
//            model.addAttribute("languages", List.of("Английский"));
//            model.addAttribute("levels", List.of("A1","A2","B1","B2","C1"));
//            return "register";
//        }
//
//        // Сохраняем rawPassword до хеширования
//        String rawPassword = user.getPassword();
//
//        // Хешируем и сохраняем
//        user.setPassword(passwordEncoder.encode(rawPassword));
//        user.setRole("USER");
//        userRepository.save(user);
//
//        // Аутентификация (автовход)
//        UsernamePasswordAuthenticationToken authReq =
//                new UsernamePasswordAuthenticationToken(user.getUsername(), rawPassword);
//        Authentication auth = authenticationManager.authenticate(authReq);
//        SecurityContextHolder.getContext().setAuthentication(auth);
//
//        return "redirect:/";
//    }
//
//    @GetMapping("/login")
//    public String loginPage() {
//        return "login";
//    }
//}
package com.example.controller;

import com.example.model.AppUser;
import com.example.repository.AppUserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class AuthController {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(AppUserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }


    
    @GetMapping("/register")
    public String registerPage(Model model) {
        if (!model.containsAttribute("userForm")) {
            model.addAttribute("userForm", new AppUser());
        }
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("userForm") AppUser userForm,
                           BindingResult bindingResult,
                           Model model) {

        
        if (bindingResult.hasErrors()) {
            return "register";
        }

        // проверка уникальности логина
        if (userRepository.findByUsername(userForm.getUsername()) != null) {
            model.addAttribute("error", "Пользователь с таким именем уже существует.");
            return "register";
        }

        // хешируем пароль
        userForm.setPassword(passwordEncoder.encode(userForm.getPassword()));
        userForm.setRole("USER");

        userRepository.save(userForm);

        // автологин
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userForm.getUsername(), userForm.getPassword());
        Authentication auth = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);

        return "redirect:/dashboard";
    }

    
    @GetMapping("/login")
    public String loginPage() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken)) {
            // уже залогинен
            return "redirect:/dashboard";
        }
        return "login";
    }
}
