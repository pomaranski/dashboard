package com.raptors.dashboard.clients;

import com.raptors.dashboard.model.TokenResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

@Slf4j
@Service
public class RaptorsClient {

    private static final ResponseEntity<Object> NO_COOKIE = ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("No cookie from RaptorsApp");

    private static final ResponseEntity<Object> CANNOT_CONNECT = ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Cannot connect to RaptorsApp");

    public ResponseEntity authenticate(URI uri, String login, String password) {
        try {
            HttpClient httpClient = getHttpClient(login, password);
            HttpRequest request = getRequest(uri);
            HttpResponse<String> response = sendRequest(httpClient, request);
            return response.headers().firstValue(SET_COOKIE).map(cookie ->
                    ResponseEntity.ok().body((Object) new TokenResponse(cookie)))
                    .orElse(NO_COOKIE);
        } catch (IOException | InterruptedException e) {
            log.error("Cannot connect to {}", uri, e);
            return CANNOT_CONNECT;
        }
    }

    private HttpResponse<String> sendRequest(HttpClient httpClient, HttpRequest request)
            throws IOException, InterruptedException {
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpRequest getRequest(URI uri) {
        return HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.noBody())
                .uri(uri)
                .build();
    }

    private HttpClient getHttpClient(String login, String password) {
        return HttpClient.newBuilder()
                .authenticator(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(login, password.toCharArray());
                    }
                }).build();
    }
}
