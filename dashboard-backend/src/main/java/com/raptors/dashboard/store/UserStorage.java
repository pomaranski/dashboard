package com.raptors.dashboard.store;

import com.raptors.dashboard.entities.User;
import com.raptors.dashboard.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserStorage {

    private final UserRepository userRepository;

    public UserStorage(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByLoginOrThrowException(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public void storeUser(User user) {
        userRepository.save(user);
    }

    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }
}
