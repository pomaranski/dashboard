package com.raptors.dashboard.integration;

import com.raptors.dashboard.model.CredentialsResponse;
import com.raptors.dashboard.model.InstanceResponse;
import com.raptors.dashboard.model.TokenResponse;
import lombok.Getter;

import java.util.List;

import static com.raptors.dashboard.integration.Requests.whenAddInstance;
import static com.raptors.dashboard.integration.Requests.whenGetCredentials;
import static com.raptors.dashboard.integration.Requests.whenGetInstances;
import static com.raptors.dashboard.integration.Requests.whenLogin;
import static com.raptors.dashboard.integration.Requests.whenRegisterOwner;
import static com.raptors.dashboard.integration.Requests.whenRemoveInstance;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

@Getter
public class TestOwner {

    protected String token;
    private String login;
    private String password;
    private String key;

    protected TestOwner(String login, String password, String key) {
        this.login = login;
        this.password = password;
        this.key = key;
    }

    public static TestOwner create() {
        return new TestOwner(
                getRandomString(),
                getRandomString(),
                getRandomHex());
    }

    public static String getRandomHex() {
        return randomNumeric(32);
    }

    public static String getRandomString() {
        return randomAlphabetic(10);
    }

    public TestOwner register() {
        whenRegisterOwner(this)
                .then()
                .statusCode(200);

        return this;
    }

    public TestOwner login() {
        TokenResponse tokenResponse = whenLogin(this)
                .then()
                .statusCode(200)
                .extract()
                .as(TokenResponse.class);

        this.token = tokenResponse.getToken();

        return this;
    }

    public InstanceResponse addInstance() {
        return whenAddInstance(token)
                .then()
                .statusCode(200)
                .extract()
                .as(InstanceResponse.class);
    }

    public void removeInstance(String uuid) {
        whenRemoveInstance(token, uuid);
    }

    @SuppressWarnings("unchecked")
    public List<InstanceResponse> getInstances() {
        return (List<InstanceResponse>) whenGetInstances(token)
                .then()
                .statusCode(200)
                .extract()
                .as(List.class);
    }

    public CredentialsResponse getCredentials(String uuid) {
        return whenGetCredentials(token, uuid).then()
                .statusCode(200)
                .extract()
                .as(CredentialsResponse.class);
    }
}
