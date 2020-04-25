package com.raptors.dashboard.services;

import com.raptors.dashboard.entities.Instance;
import com.raptors.dashboard.entities.User;
import com.raptors.dashboard.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByLoginOrThrowException(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public void addInstance(String userId, Instance instance) {
        userRepository.findById(userId)
                .ifPresent(user -> {
                    user.addInstance(instance);
                    userRepository.save(user);
                });
    }

    public void removeInstance(String userId, UUID uuid) {
        userRepository.findById(userId)
                .ifPresent(user -> {
                    user.removeInstance(uuid);
                    userRepository.save(user);
                });
    }
}
