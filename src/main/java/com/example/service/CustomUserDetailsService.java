package com.example.service;

import com.example.model.AppUser;
import com.example.repository.AppUserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AppUserRepository userRepository;

    public CustomUserDetailsService(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AppUser user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }
//@Override
//public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//    System.out.println("DEBUG: loadUserByUsername called with username = " + username);
//
//    AppUser user = userRepository.findByUsername(username);
//
//    if (user == null) {
//        System.out.println("DEBUG: user not found for username = " + username);
//        throw new UsernameNotFoundException("User not found");
//    }
//
//    System.out.println("DEBUG: found user: username=" + user.getUsername()
//            + ", role=" + user.getRole()
//            + ", passwordHashStartsWith=" + (user.getPassword() != null ? user.getPassword().substring(0, Math.min(6, user.getPassword().length())) : "null"));
//
//    return org.springframework.security.core.userdetails.User
//            .withUsername(user.getUsername())
//            .password(user.getPassword())
//            .roles(user.getRole())
//            .build();
//}

}
