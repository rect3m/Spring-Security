package com.rect2m.service;

import com.rect2m.entity.User;
import com.rect2m.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean registerUser(User user) {
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            return false; // Користувач вже існує
        }
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Хешування паролю
        userRepository.save(user);
        return true;
    }
}

