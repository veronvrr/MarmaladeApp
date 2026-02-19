package com.example.service;

import com.example.model.AppUser;
import com.example.repository.AppUserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final AppUserRepository userRepository;

    public UserService(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AppUser findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public AppUser save(AppUser user) {
        return userRepository.save(user);
    }
}
