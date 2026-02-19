//
//package com.example.model;
//
//import jakarta.persistence.*;
//
//@Entity
//public class AppUser {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String username;
//    private String password;
//    private String role; // USER или ADMIN
//
//    // Новые поля
//    private String fullName;
//    private String preferredLanguage;
//    private String level;
//
//    public AppUser() {}
//
//    public AppUser(String username, String password, String role) {
//        this.username = username;
//        this.password = password;
//        this.role = role;
//    }
//
//    // Полный конструктор (удобно для seed)
//    public AppUser(String username, String password, String role, String fullName, String preferredLanguage, String level) {
//        this.username = username;
//        this.password = password;
//        this.role = role;
//        this.fullName = fullName;
//        this.preferredLanguage = preferredLanguage;
//        this.level = level;
//    }
//
//    // геттеры / сеттеры
//
//    public Long getId() {
//        return id;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getRole() {
//        return role;
//    }
//
//    public void setRole(String role) {
//        this.role = role;
//    }
//
//    public String getFullName() {
//        return fullName;
//    }
//
//    public void setFullName(String fullName) {
//        this.fullName = fullName;
//    }
//
//    public String getPreferredLanguage() {
//        return preferredLanguage;
//    }
//
//    public void setPreferredLanguage(String preferredLanguage) {
//        this.preferredLanguage = preferredLanguage;
//    }
//
//    public String getLevel() {
//        return level;
//    }
//
//    public void setLevel(String level) {
//        this.level = level;
//    }
//}

package com.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "app_user")
public class AppUser {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @NotBlank(message = "Логин обязателен")
    @Size(min = 3, max = 50, message = "Логин должен быть от 3 до 50 символов")
    @Column(unique = true) private String username;
    @NotBlank(message = "Пароль обязателен")
    @Size(min = 6, message = "Пароль должен быть не короче 6 символов")
    private String password;
    @Column(unique = true)
//    private String username;   // логин — используется для входа
//
//    private String password;   // захэшированный пароль

    private String role;       // "USER" или "ADMIN"

    @Column(name = "full_name")
    private String fullName;   // отображаемое имя (можно редактировать в профиле)

    @Column(name = "preferred_language")
    private String preferredLanguage; // пр"en"

    private String level;      // A1..C1

    public AppUser() {}
    public AppUser(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public AppUser(String username, String password, String role,
                   String fullName, String preferredLanguage, String level) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.fullName = fullName;
        this.preferredLanguage = preferredLanguage;
        this.level = level;
    }


    public Long getId() {
        return id;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Column(name = "avatar_filename")
    private String avatarFilename;

    public String getAvatarFilename() { return avatarFilename; }
    public void setAvatarFilename(String avatarFilename) { this.avatarFilename = avatarFilename; }

}
