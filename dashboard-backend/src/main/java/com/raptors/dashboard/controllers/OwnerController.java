package com.raptors.dashboard.controllers;

import com.raptors.dashboard.model.InstanceRequest;
import com.raptors.dashboard.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = OwnerController.OWNER_PATH,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE)
public class OwnerController {

    static final String OWNER_PATH = "/owner";
    private static final String INSTANCE = "/instance";
    private static final String INSTANCE_ID = "/instance/{id}";

    private final UserService userService;

    public OwnerController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(INSTANCE)
    public ResponseEntity addInstance(@RequestBody InstanceRequest instanceRequest,
                                      Principal principal) {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
        return userService.addInstance((String) token.getPrincipal(), (String) token.getCredentials(), instanceRequest);
    }

    @DeleteMapping(INSTANCE_ID)
    public ResponseEntity addInstance(@PathVariable("id") String id,
                                      Principal principal) {
        return userService.removeInstance(principal.getName(), id);
    }
}
