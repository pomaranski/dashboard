package com.raptors.dashboard.repositories;

import com.raptors.dashboard.entities.User;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<User, String> {

    Optional<User> findByLogin(String login);
}
