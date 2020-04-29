package com.raptors.dashboard.ssh;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.raptors.dashboard.model.CommandFailedResult;
import com.raptors.dashboard.model.ConnectionProblemResult;
import com.raptors.dashboard.model.ExecutionResult;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class InstanceSshConnector {

    public static final String STRICT_HOST_KEY_CHECKING = "StrictHostKeyChecking";
    public static final String EXEC_CHANNEL = "exec";

    public Either<ExecutionResult, Integer> executeScript(URI uri, String hostUser, String password, String command) {
        Session session = null;
        try {
            session = new JSch().getSession(hostUser, uri.getHost(), uri.getPort());
            session.setPassword(password);

            session.setConfig(STRICT_HOST_KEY_CHECKING, "no");
            session.connect();

            ChannelExec exec = (ChannelExec) session.openChannel(EXEC_CHANNEL);
            exec.setCommand(command);
            exec.connect();
            String errorOutput = new String(exec.getErrStream().readAllBytes(), StandardCharsets.UTF_8.name());
            log.info("Executed command output: {}", errorOutput);

            int exitStatus = exec.getExitStatus();
            if (exitStatus == 0) {
                return Either.right(exitStatus);
            } else {
                log.info("Command execution failed. Command: {}, exit code: {}", command, exitStatus);
                return Either.left(new CommandFailedResult());
            }
        } catch (JSchException | IOException e) {
            log.info("Couldn't connect through ssh", e);
            return Either.left(new ConnectionProblemResult());
        } finally {
            if (session != null) {
                session.disconnect();
            }
        }
    }
}
