package com.raptors.dashboard.controllers;

import com.raptors.dashboard.model.RegisterRequest;
import com.raptors.dashboard.security.CustomToken;
import com.raptors.dashboard.services.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = AdminController.ADMIN_PATH,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE)
public class AdminController {

    static final String ADMIN_PATH = "/admin";

    private static final String INSTANCE_UNASSIGNED = "/instance/unassigned";
    private static final String REGISTER = "/register";

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping(INSTANCE_UNASSIGNED)
    public ResponseEntity getUnassigned(CustomToken customToken) {
        return adminService.getAllUnAssignedUri(customToken.getPrincipal());
    }

    @PostMapping(REGISTER)
    public ResponseEntity register(@RequestBody RegisterRequest registerRequest) {
        return adminService.register(registerRequest);
    }
}
