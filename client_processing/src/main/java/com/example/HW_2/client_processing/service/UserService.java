package com.example.HW2.client_processing.service;

import com.example.HW2.client_processing.entity.User;
import com.example.HW2.client_processing.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User createUser(Long userId, String login, String password, String email) {
        User user = User.builder()
                .login(login)
                .password(password) // TODO: добавить шифрование
                .email(email)
                .build();
        return userRepository.save(user);
    }
}
