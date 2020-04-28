package com.raptors.dashboard.controllers;

import com.raptors.dashboard.model.InstanceRequest;
import com.raptors.dashboard.security.CustomToken;
import com.raptors.dashboard.services.AuthorizedUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = AuthorizedUserController.USER_PATH,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE)
public class AuthorizedUserController {

    static final String USER_PATH = "/user";

    private static final String INSTANCE = "/instance";
    private static final String INSTANCE_UUID_LOGIN = "/instance/{uuid}/login";
    private static final String INSTANCE_UUID = "/instance/{uuid}";

    private final AuthorizedUserService authorizedUserService;

    public AuthorizedUserController(AuthorizedUserService authorizedUserService) {
        this.authorizedUserService = authorizedUserService;
    }

    @GetMapping(INSTANCE)
    public ResponseEntity getInstances(CustomToken token) {
        return authorizedUserService.getInstances(token.getPrincipal());
    }

    @PostMapping(INSTANCE_UUID_LOGIN)
    public ResponseEntity loginToInstance(@PathVariable("uuid") String uuid,
                                          CustomToken token) {
        return authorizedUserService.loginToInstance(token.getPrincipal(), token.getKey(), uuid);
    }

    @DeleteMapping(INSTANCE_UUID)
    public ResponseEntity removeInstance(@PathVariable("uuid") String uuid,
                                         CustomToken token) {
        return authorizedUserService.removeInstance(token.getPrincipal(), uuid);
    }

    @PostMapping(INSTANCE)
    public ResponseEntity addInstance(@RequestBody InstanceRequest instanceRequest,
                                      CustomToken token) {
        return authorizedUserService.addInstance(token.getPrincipal(), token.getKey(), instanceRequest);
    }
}
