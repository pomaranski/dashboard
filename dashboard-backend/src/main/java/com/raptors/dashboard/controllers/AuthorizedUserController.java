package com.raptors.dashboard.controllers;

import com.raptors.dashboard.model.CredentialsRequest;
import com.raptors.dashboard.model.ExecuteCommandRequest;
import com.raptors.dashboard.model.InstanceRequest;
import com.raptors.dashboard.security.CustomToken;
import com.raptors.dashboard.services.AuthorizedUserService;
import com.raptors.dashboard.services.SshService;
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
    private static final String UUID = "uuid";
    private static final String INSTANCE_UUID_CREDENTIALS = "/instance/{" + UUID + "}/credentials";
    private static final String INSTANCE_UUID = "/instance/{" + UUID + "}";
    private static final String EXECUTE = INSTANCE_UUID + "/execute";

    private final AuthorizedUserService authorizedUserService;
    private final SshService sshService;

    public AuthorizedUserController(AuthorizedUserService authorizedUserService, SshService sshService) {
        this.authorizedUserService = authorizedUserService;
        this.sshService = sshService;
    }

    @GetMapping(INSTANCE)
    public ResponseEntity getInstances(CustomToken token) {
        return authorizedUserService.getInstances(token.getPrincipal());
    }

    @PostMapping(INSTANCE_UUID_CREDENTIALS)
    public ResponseEntity getCredentials(@PathVariable(UUID) String uuid,
                                         @RequestBody CredentialsRequest credentialsRequest,
                                         CustomToken token) {
        return authorizedUserService.getCredentials(token.getPrincipal(), token.getKey(), uuid, credentialsRequest);
    }

    @DeleteMapping(INSTANCE_UUID)
    public ResponseEntity removeInstance(@PathVariable(UUID) String uuid,
                                         CustomToken token) {
        return authorizedUserService.removeInstance(token.getPrincipal(), uuid);
    }

    @PostMapping(INSTANCE)
    public ResponseEntity addInstance(@RequestBody InstanceRequest instanceRequest,
                                      CustomToken token) {
        return authorizedUserService.addInstance(token.getPrincipal(), token.getKey(), instanceRequest);
    }

    @PostMapping(EXECUTE)
    public ResponseEntity executeCommand(@PathVariable(UUID) String instanceUuid,
            @RequestBody ExecuteCommandRequest executeCommandRequest,
            CustomToken token) {
        return sshService.executeCommand(token.getPrincipal(), token.getKey(), instanceUuid, executeCommandRequest);
    }
}
