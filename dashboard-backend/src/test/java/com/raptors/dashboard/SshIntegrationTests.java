package com.raptors.dashboard;

import com.raptors.dashboard.integration.Requests;
import com.raptors.dashboard.integration.TestOwner;
import com.raptors.dashboard.model.CommandFailedResult;
import com.raptors.dashboard.model.ConnectionProblemResult;
import com.raptors.dashboard.model.InstanceResponse;
import com.raptors.dashboard.services.SshService;
import com.raptors.dashboard.ssh.InstanceSshConnector;
import io.vavr.control.Either;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.net.URI;
import java.util.Set;

import static com.raptors.dashboard.integration.Requests.whenExecuteCommand;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static java.net.HttpURLConnection.HTTP_UNAVAILABLE;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(SpringRunner.class)
@ComponentScan
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class SshIntegrationTests {

    @Value("${server.port}")
    private int port;

    @Value("${management.server.port}")
    private int managementPort;

    @Autowired
    private Set<CrudRepository> repositories;

    @Autowired
    private SshService sshService;

    @Mock
    InstanceSshConnector instanceSshConnector;

    @Before
    public void setup() {
        initMocks(this);
        ReflectionTestUtils.setField(sshService, "instanceSshConnector", instanceSshConnector);
        Requests.port = port;
        Requests.managementPort = managementPort;
        repositories.forEach(CrudRepository::deleteAll);
    }

    @Test
    public void shouldExecuteSsh() {
        when(instanceSshConnector.executeScript(any(URI.class), anyString(), anyString(), anyString())).thenReturn(Either.right(0));
        TestOwner testOwner = TestOwner.create()
                .register()
                .login();
        InstanceResponse instance = testOwner
                .addInstance();

        whenExecuteCommand(testOwner.getToken(), instance)
                .then()
                .statusCode(HTTP_OK);

    }

    @Test
    public void shouldReturnBadRequestWhenExecuteSshReturnErrorCode() {
        when(instanceSshConnector.executeScript(any(URI.class), anyString(), anyString(), anyString()))
                .thenReturn(Either.left(new CommandFailedResult()));
        TestOwner testOwner = TestOwner.create()
                .register()
                .login();
        InstanceResponse instance = testOwner
                .addInstance();

        whenExecuteCommand(testOwner.getToken(), instance)
                .then()
                .statusCode(HTTP_BAD_REQUEST);

    }

    @Test
    public void shouldReturnServiceUnavailableWhenSshCantConnect() {
        when(instanceSshConnector.executeScript(any(URI.class), anyString(), anyString(), anyString()))
                .thenReturn(Either.left(new ConnectionProblemResult()));
        TestOwner testOwner = TestOwner.create()
                .register()
                .login();
        InstanceResponse instance = testOwner
                .addInstance();

        whenExecuteCommand(testOwner.getToken(), instance)
                .then()
                .statusCode(HTTP_UNAVAILABLE);

    }

    @Test
    public void shouldNotExecuteWhenTokenIsNotProvided() {
        TestOwner testOwner = TestOwner.create()
                .register()
                .login();
        InstanceResponse instance = testOwner
                .addInstance();

        whenExecuteCommand(EMPTY, instance)
                .then()
                .statusCode(HTTP_UNAUTHORIZED);
    }

    @Test
    public void shouldNotFindInstance() {
        TestOwner testOwner = TestOwner.create()
                .register()
                .login();

        InstanceResponse otherOwnerInstance = TestOwner.create()
                .register()
                .login()
                .addInstance();

        whenExecuteCommand(testOwner.getToken(), otherOwnerInstance)
                .then()
                .statusCode(HTTP_NOT_FOUND);
    }

    @Test
    public void shouldNotProcessWhenCommandNotProvided() {
        TestOwner testOwner = TestOwner.create()
                .register()
                .login();

        InstanceResponse instance = testOwner.addInstance();

        whenExecuteCommand(testOwner.getToken(), instance, null)
                .then()
                .statusCode(HTTP_BAD_REQUEST);
    }

    @Test
    public void shouldNotProcessWhenCommandIsEmptyProvided() {
        TestOwner testOwner = TestOwner.create()
                .register()
                .login();

        InstanceResponse instance = testOwner.addInstance();

        whenExecuteCommand(testOwner.getToken(), instance, "")
                .then()
                .statusCode(HTTP_BAD_REQUEST);
    }
}
