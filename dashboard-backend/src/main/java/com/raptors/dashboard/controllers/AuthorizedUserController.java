package com.raptors.dashboard.controllers;

import com.raptors.dashboard.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = AuthorizedUserController.USER_PATH,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE)
public class AuthorizedUserController {

    static final String USER_PATH = "/user";

    private static final String INSTANCE = "/instance";

    private final UserService userService;

    public AuthorizedUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(INSTANCE)
    public ResponseEntity getInstances(Principal principal) {
        return userService.getInstances(principal.getName());
    }
}
