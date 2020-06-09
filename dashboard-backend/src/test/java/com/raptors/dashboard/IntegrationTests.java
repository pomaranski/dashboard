package com.raptors.dashboard;

import com.raptors.dashboard.integration.Requests;
import com.raptors.dashboard.integration.TestAdmin;
import com.raptors.dashboard.integration.TestOwner;
import com.raptors.dashboard.model.AuthUser;
import com.raptors.dashboard.model.InstanceRequest;
import com.raptors.dashboard.model.InstanceResponse;
import com.raptors.dashboard.model.RegisterRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;
import java.util.UUID;

import static com.raptors.dashboard.integration.Requests.randomInstanceRequest;
import static com.raptors.dashboard.integration.Requests.whenAddInstance;
import static com.raptors.dashboard.integration.Requests.whenGetCredentials;
import static com.raptors.dashboard.integration.Requests.whenGetInstances;
import static com.raptors.dashboard.integration.Requests.whenGetUnassignedInstances;
import static com.raptors.dashboard.integration.Requests.whenLogin;
import static com.raptors.dashboard.integration.Requests.whenRegisterOwner;
import static com.raptors.dashboard.integration.Requests.whenRemoveInstance;
import static com.raptors.dashboard.integration.TestOwner.getRandomHex;
import static com.raptors.dashboard.integration.TestOwner.getRandomString;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_CONFLICT;
import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@ComponentScan
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class IntegrationTests {

    @Value("${server.port}")
    private int port;

    @Value("${management.server.port}")
    private int managementPort;

    @Autowired
    private Set<CrudRepository> repositories;

    @Before
    public void setUp() {
        Requests.port = port;
        Requests.managementPort = managementPort;
        repositories.forEach(CrudRepository::deleteAll);
    }

    @Test
    public void shouldLoginUser() {
        TestOwner.create()
                .register()
                .login();
    }

    @Test
    public void shouldNotLoginUserWhenWrongLogin() {
        TestOwner testOwner = TestOwner.create()
                .register();

        whenLogin(AuthUser.builder()
                .login(getRandomString())
                .password(testOwner.getPassword())
                .key(testOwner.getKey())
                .build())
                .then()
                .statusCode(HTTP_UNAUTHORIZED);
    }

    @Test
    public void shouldNotLoginUserWhenWrongPassword() {
        TestOwner testOwner = TestOwner.create()
                .register();

        whenLogin(AuthUser.builder()
                .login(testOwner.getLogin())
                .password(getRandomString())
                .key(testOwner.getKey())
                .build())
                .then()
                .statusCode(HTTP_UNAUTHORIZED);
    }

    @Test
    public void shouldNotLoginUserWhenWrongKey() {
        TestOwner testOwner = TestOwner.create()
                .register();

        whenLogin(AuthUser.builder()
                .login(testOwner.getLogin())
                .password(testOwner.getPassword())
                .key(getRandomHex())
                .build())
                .then()
                .statusCode(HTTP_UNAUTHORIZED);
    }

    @Test
    public void shouldAddInstanceToLoggedInUser() {
        TestOwner.create()
                .register()
                .login()
                .addInstance();
    }

    @Test
    public void shouldNotAddInstanceToLoggedInUserWhenWrongData() {
        TestOwner testOwner = TestOwner.create()
                .register()
                .login();

        InstanceRequest instanceRequest = randomInstanceRequest();
        instanceRequest.setInstancePassword(EMPTY);

        whenAddInstance(testOwner.getToken(), instanceRequest)
                .then()
                .statusCode(HTTP_BAD_REQUEST);
    }

    @Test
    public void shouldNotAddInstanceWhenEmptyToken() {
        whenAddInstance(EMPTY, randomInstanceRequest())
                .then()
                .statusCode(HTTP_UNAUTHORIZED);
    }

    @Test
    public void shouldAddAndRemoveInstanceFromLoggedInUser() {
        TestOwner testOwner = TestOwner.create()
                .register()
                .login();

        InstanceResponse instanceResponse = testOwner.addInstance();

        testOwner.removeInstance(instanceResponse.getUuid());
    }

    @Test
    public void shouldNotRemoveInstanceFromLoggedInUserWhenInstanceNotExists() {
        TestOwner testOwner = TestOwner.create()
                .register()
                .login();

        whenRemoveInstance(testOwner.getToken(), UUID.randomUUID().toString())
                .then()
                .statusCode(HTTP_NOT_FOUND);
    }

    @Test
    public void shouldNotRemoveInstanceWhenEmptyToken() {
        TestOwner testOwner = TestOwner.create()
                .register()
                .login();

        InstanceResponse instanceResponse = testOwner.addInstance();

        whenRemoveInstance(EMPTY, instanceResponse.getUuid())
                .then()
                .statusCode(HTTP_UNAUTHORIZED);
    }

    @Test
    public void shouldAddAndGetInstancesToLoggedInUser() {
        TestOwner testOwner = TestOwner.create()
                .register()
                .login();

        testOwner.addInstance();
        testOwner.addInstance();

        assertEquals(2, testOwner.getInstances().size());
    }

    @Test
    public void shouldNotGetInstancesWhenTokenEmpty() {
        whenGetInstances(EMPTY)
                .then()
                .statusCode(HTTP_UNAUTHORIZED);
    }

    @Test
    public void shouldAddInstanceAndGetCredentialsToLoggedInUser() {
        TestOwner testOwner = TestOwner.create()
                .register()
                .login();

        InstanceResponse instanceResponse = testOwner.addInstance();

        testOwner.getCredentials(instanceResponse.getUuid());
    }

    @Test
    public void shouldNotGetCredentialsToLoggedInUserWhenInstanceNotExists() {
        TestOwner testOwner = TestOwner.create()
                .register()
                .login();

        whenGetCredentials(testOwner.getToken(), UUID.randomUUID().toString())
                .then()
                .statusCode(HTTP_NOT_FOUND);
    }

    @Test
    public void shouldNotGetCredentialsToLoggedInUserWhenWrongData() {
        TestOwner testOwner = TestOwner.create()
                .register()
                .login();

        InstanceResponse instanceResponse = testOwner.addInstance();

        whenGetCredentials(testOwner.getToken(), instanceResponse.getUuid(), EMPTY)
                .then()
                .statusCode(HTTP_BAD_REQUEST);
    }

    @Test
    public void shouldNotGetCredentialsWhenTokenEmpty() {
        TestOwner testOwner = TestOwner.create()
                .register()
                .login();

        InstanceResponse instanceResponse = testOwner.addInstance();

        whenGetCredentials(EMPTY, instanceResponse.getUuid())
                .then()
                .statusCode(HTTP_UNAUTHORIZED);
    }

    @Test
    public void shouldReturnUnassignedInstancesToAdmin() {
        TestAdmin admin1 = TestAdmin.create();
        TestAdmin admin2 = TestAdmin.create();

        admin1.register()
                .login();

        admin2.register()
                .login();

        admin1.addInstance();

        assertEquals(0, admin1.getUnassignedInstances().size());
        assertEquals(1, admin2.getUnassignedInstances().size());
    }

    @Test
    public void shouldNotReturnUnassignedInstancesWhenUserIsOwner() {
        TestOwner testOwner = TestOwner.create();

        testOwner.register()
                .login();

        whenGetUnassignedInstances(testOwner.getToken())
                .then()
                .statusCode(HTTP_FORBIDDEN);
    }

    @Test
    public void shouldNotReturnUnassignedInstancesWhenEmptyToken() {
        whenGetUnassignedInstances(EMPTY)
                .then()
                .statusCode(HTTP_UNAUTHORIZED);
    }

    @Test
    public void shouldRegisterOwnerAndLogIn() {
        TestAdmin admin = TestAdmin.create();
        admin.register();
        admin.login();
        admin.registerOwner(RegisterRequest.builder()
                .login("login")
                .hashedPassword("$2a$10$v6ATog4uS0U5sdgwpjKduOAXu34TjPJcNTSBBUH8dKbHI9umOrUey")
                .hashedKey("$2a$10$hLYcrXJEQqPYTwDbsWe8l.9TNh5KW8NPPr5/esREQCFsDYpuWV8ry")
                .build());

        whenLogin(AuthUser.builder()
                .login("login")
                .password("password")
                .key("c28dc589b7e96ea51c357649aeb42d98")
                .build())
                .then()
                .statusCode(HTTP_OK);
    }

    @Test
    public void shouldNotRegisterOwnerWhenLoginExists() {
        TestAdmin admin = TestAdmin.create();
        admin.register();
        admin.login();

        whenRegisterOwner(admin.getToken(), RegisterRequest.builder()
                .login(admin.getLogin())
                .hashedPassword("$2a$10$v6ATog4uS0U5sdgwpjKduOAXu34TjPJcNTSBBUH8dKbHI9umOrUey")
                .hashedKey("$2a$10$hLYcrXJEQqPYTwDbsWe8l.9TNh5KW8NPPr5/esREQCFsDYpuWV8ry")
                .build())
                .then()
                .statusCode(HTTP_CONFLICT);
    }

    @Test
    public void shouldNotRegisterOwnerWhenUserIsOwner() {
        TestOwner owner = TestOwner.create();
        owner.register();
        owner.login();
        whenRegisterOwner(owner.getToken(), RegisterRequest.builder()
                .login("login")
                .hashedPassword("$2a$10$v6ATog4uS0U5sdgwpjKduOAXu34TjPJcNTSBBUH8dKbHI9umOrUey")
                .hashedKey("$2a$10$hLYcrXJEQqPYTwDbsWe8l.9TNh5KW8NPPr5/esREQCFsDYpuWV8ry")
                .build())
                .then()
                .statusCode(HTTP_FORBIDDEN);
    }

    @Test
    public void shouldNotRegisterOwnerWhenWrongData() {
        TestAdmin admin = TestAdmin.create();
        admin.register();
        admin.login();

        whenRegisterOwner(admin.getToken(), RegisterRequest.builder()
                .login("login")
                .hashedKey("$2a$10$hLYcrXJEQqPYTwDbsWe8l.9TNh5KW8NPPr5/esREQCFsDYpuWV8ry")
                .build())
                .then()
                .statusCode(HTTP_BAD_REQUEST);
    }

    @Test
    public void shouldNotRegisterOwnerWhenNoToken() {
        whenRegisterOwner(EMPTY, RegisterRequest.builder()
                .login("login")
                .hashedPassword("$2a$10$v6ATog4uS0U5sdgwpjKduOAXu34TjPJcNTSBBUH8dKbHI9umOrUey")
                .hashedKey("$2a$10$hLYcrXJEQqPYTwDbsWe8l.9TNh5KW8NPPr5/esREQCFsDYpuWV8ry")
                .build())
                .then()
                .statusCode(HTTP_UNAUTHORIZED);
    }
}
