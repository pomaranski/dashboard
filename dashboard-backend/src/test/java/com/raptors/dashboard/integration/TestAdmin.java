package com.raptors.dashboard.integration;

import com.raptors.dashboard.model.RegisterRequest;

import java.util.List;

import static com.raptors.dashboard.integration.Requests.whenGetUnassignedInstances;
import static com.raptors.dashboard.integration.Requests.whenRegisterAdmin;
import static com.raptors.dashboard.integration.Requests.whenRegisterOwner;

public class TestAdmin extends TestOwner {

    TestAdmin(String login, String password, String key) {
        super(login, password, key);
    }

    public static TestAdmin create() {
        return new TestAdmin(
                getRandomString(),
                getRandomString(),
                getRandomHex());
    }

    @Override
    public TestOwner register() {
        whenRegisterAdmin(this)
                .then()
                .statusCode(200);

        return this;
    }

    @SuppressWarnings("unchecked")
    public List<String> getUnassignedInstances() {
        return (List<String>) whenGetUnassignedInstances(token)
                .then()
                .statusCode(200)
                .extract()
                .as(List.class);
    }

    public void registerOwner(RegisterRequest registerRequest) {
        whenRegisterOwner(token, registerRequest)
                .then()
                .statusCode(200);
    }
}
