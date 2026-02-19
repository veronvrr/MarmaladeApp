package com.example;

import com.example.model.AppUser;
import com.example.model.QuestionEntity;
import com.example.repository.AppUserRepository;
import com.example.repository.QuestionRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import com.example.model.Teacher;
import com.example.repository.TeacherRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class MarmaladeApplication {
    public static void main(String[] args) {
        SpringApplication.run(MarmaladeApplication.class, args);

    }
    @Bean
    public CommandLineRunner seedAdmin(AppUserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String adminUsername = "admin";
            String adminPassword = "admin123";
            if (userRepository.findByUsername(adminUsername) == null) {
                AppUser admin = new AppUser(adminUsername, passwordEncoder.encode(adminPassword), "ADMIN");
                userRepository.save(admin);
                System.out.println("Seed admin created: username=" + adminUsername + " password=" + adminPassword);
            } else {
                System.out.println("Admin already exists, skipping seed.");
            }
        };
    }
    @Bean
    public CommandLineRunner seedQuestions(QuestionRepository repo) {
        return args -> {
            if(repo.count() == 0) {
                // A1
                repo.save(new QuestionEntity("A1", "Переведи: 'Hello' →", null, "привет|hello"));
                repo.save(new QuestionEntity("A1", "Complete: I ___ (be) a student.", null, "am"));
                repo.save(new QuestionEntity("A1", "Выбери верный вариант:", "He go,He goes,He going", "He goes"));

                // B1
                repo.save(new QuestionEntity("B1", "If I ___ (win) the lottery, I would travel.", null, "won"));
                repo.save(new QuestionEntity("B1", "Choose phrasal verb meaning 'postpone':", "put off,give up,take off", "put off"));

                System.out.println("Seeded sample questions");
            } else {
                System.out.println("Questions already exist, skipping seed");
            }
        };
    }

}
