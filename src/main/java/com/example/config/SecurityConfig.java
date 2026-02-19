//
//package com.example.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/", "/index", "/login", "/register", "/css/**",  "/images/**", "/fonts/**").permitAll()
//                        .requestMatchers("/admin/**").hasRole("ADMIN")
//                        .anyRequest().authenticated()
//                )
//                .formLogin(login -> login
//                        .loginPage("/login")
//                        .defaultSuccessUrl("/home", true)
//                        .permitAll()
//                )
//                .logout(logout -> logout
//                        .logoutUrl("/logout")
//                        .logoutSuccessUrl("/")
//                        .deleteCookies("JSESSIONID")
//                        .invalidateHttpSession(true)
//                        .permitAll()
//                );
//
//        return http.build();
//    }
//}

package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // разрешаем доступ к статике и публичным страницам
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/index", "/home", "/login", "/register",
                                "/css/**", "/js/**", "/images/**", "/fonts/**",
                                "/favicon.ico", "/webjars/**", "/avatars/**", "/modules/**", "/api/**", "/audios/**", "/videos/**", "/teachers/**", "/teachers/**", "/users/**"
                        ).permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/my/**").authenticated()
                        .anyRequest().authenticated()
                )

                // форма логина
                .formLogin(login -> login
                        .loginPage("/login")            // страница логина (GET)
                        .loginProcessingUrl("/login")   // endpoint, на который отправляется POST формы
                        .defaultSuccessUrl("/dashboard", true)   // куда редиректить после успешного логина
                        .permitAll()
                )

                // logout
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                        .permitAll()
                );

        // Оставляем CSRF включённым (по умолчанию) — формы должны содержать токен.
        return http.build();
    }
}
