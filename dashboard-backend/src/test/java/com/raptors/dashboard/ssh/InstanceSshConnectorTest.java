package com.raptors.dashboard.ssh;

import com.raptors.dashboard.model.CommandFailedResult;
import com.raptors.dashboard.model.ConnectionProblemResult;
import com.raptors.dashboard.model.ExecutionResult;
import io.vavr.control.Either;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.net.URI;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

public class InstanceSshConnectorTest {

    @Test
    @Ignore
    public void shouldExecuteSshCommand() {
        Either<ExecutionResult, Integer> integers = new InstanceSshConnector()
                .executeScript(URI.create("ssh://localhost:22"), "ssh_test", "test", "bash test.sh");

        assertEquals(Integer.valueOf(0), integers.get());
    }

    @Test
    @Ignore
    public void shouldReturnCommandExecuteError() {
        Either<ExecutionResult, Integer> integers = new InstanceSshConnector()
                .executeScript(URI.create("ssh://localhost:22"), "ssh_test", "test", "bumcfksz");

        assertThat(integers.getLeft()).isInstanceOf(CommandFailedResult.class);
    }

    @Test
    @Ignore
    public void shouldReturnConnectionExecuteError() {
        Either<ExecutionResult, Integer> integers = new InstanceSshConnector()
                .executeScript(URI.create("ssh://localhost:22"), "ssh_test", "test", "bumcfksz");

        assertThat(integers.getLeft()).isInstanceOf(ConnectionProblemResult.class);
    }
}