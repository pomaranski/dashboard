package com.raptors.dashboard.integration;

import com.raptors.dashboard.model.AuthUser;
import com.raptors.dashboard.model.CredentialsRequest;
import com.raptors.dashboard.model.ExecuteCommandRequest;
import com.raptors.dashboard.model.InstanceRequest;
import com.raptors.dashboard.model.InstanceResponse;
import com.raptors.dashboard.model.RegisterRequest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

import static com.raptors.dashboard.crytpo.HexUtils.bytesToHex;
import static io.restassured.RestAssured.given;

public class Requests {

    public static int managementPort;
    public static int port;

    public static Response whenRegisterOwner(TestOwner testOwner) {
        return given()
                .port(managementPort)
                .contentType(ContentType.JSON)
                .body(AuthUser.builder()
                        .login(testOwner.getLogin())
                        .password(testOwner.getPassword())
                        .key(testOwner.getKey())
                        .build())
                .post("/dashboard/register/owner");
    }

    public static Response whenRegisterAdmin(TestAdmin testAdmin) {
        return given()
                .port(managementPort)
                .contentType(ContentType.JSON)
                .body(AuthUser.builder()
                        .login(testAdmin.getLogin())
                        .password(testAdmin.getPassword())
                        .key(testAdmin.getKey())
                        .build())
                .post("/dashboard/register/admin");
    }

    public static Response whenLogin(TestOwner testOwner) {
        return whenLogin(AuthUser.builder()
                .login(testOwner.getLogin())
                .password(testOwner.getPassword())
                .key(testOwner.getKey())
                .build());
    }

    public static Response whenLogin(AuthUser authUser) {
        return given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(authUser)
                .post("/login");
    }

    public static Response whenAddInstance(String token) {
        return whenAddInstance(token, randomInstanceRequest());
    }

    public static Response whenAddInstance(String token, InstanceRequest instanceRequest) {
        return given()
                .port(port)
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .body(instanceRequest)
                .post("/user/instance");
    }

    public static Response whenRemoveInstance(String token, String uuid) {
        return given()
                .port(port)
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .delete("/user/instance/" + uuid);
    }

    public static Response whenGetInstances(String token) {
        return given()
                .port(port)
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .get("/user/instance/");
    }

    public static Response whenGetCredentials(String token, String uuid) {
        return whenGetCredentials(token, uuid, bytesToHex(getKeyPair().getPublic().getEncoded()));
    }

    public static Response whenGetCredentials(String token, String uuid, String publicKey) {
        return given()
                .port(port)
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .body(CredentialsRequest.builder()
                        .publicKey(publicKey)
                        .build())
                .post("/user/instance/" + uuid + "/credentials");
    }

    public static Response whenGetUnassignedInstances(String token) {
        return given()
                .port(port)
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .get("/admin/instance/unassigned");
    }

    public static InstanceRequest randomInstanceRequest() {
        return InstanceRequest.builder()
                .name(randomString())
                .hostLogin(randomString())
                .hostPassword(randomString())
                .httpUri(randomHttpUri())
                .instanceLogin(randomString())
                .instancePassword(randomString())
                .sshUri(randomSshUri())
                .build();
    }

    public static Response whenExecuteCommand(String token, InstanceResponse instanceRequest) {
        return whenExecuteCommand(token, instanceRequest, "bash test.sh");
    }

    public static Response whenExecuteCommand(String token, InstanceResponse instanceRequest, String command) {
        return given()
                .port(port)
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(new ExecuteCommandRequest(command))
                .post("/user/instance/{instanceId}/execute", instanceRequest.getUuid());
    }

    public static Response whenRegisterOwner(String token, RegisterRequest registerRequest) {
        return given()
                .port(port)
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(registerRequest)
                .post("/admin/register");
    }

    private static String randomString() {
        return RandomStringUtils.randomAlphabetic(10);
    }

    @SneakyThrows
    private static URI randomHttpUri() {
        return new URIBuilder()
                .setScheme("http")
                .setHost(randomString())
                .build();
    }

    @SneakyThrows
    private static URI randomSshUri() {
        return new URIBuilder()
                .setScheme("ssh")
                .setHost(randomString())
                .build();
    }

    @SneakyThrows
    private static KeyPair getKeyPair() {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024);
        return keyGen.generateKeyPair();
    }
}
