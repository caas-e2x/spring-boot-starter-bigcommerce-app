package com.caas.spring.boot.starter.bigcommerce.app.integrations;

import com.caas.spring.boot.starter.bigcommerce.app.AuthorizationFlowController;
import com.caas.spring.boot.starter.bigcommerce.app.BigCommerceAppException;
import com.caas.spring.boot.starter.bigcommerce.app.BigCommerceApplicationConfiguration;
import com.caas.spring.boot.starter.bigcommerce.app.HttpClientFactory;
import com.caas.spring.boot.starter.bigcommerce.app.model.AuthToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
public class BigCommerceClient implements AuthorizationFlowController {

    private final BigCommerceApplicationConfiguration bigCommerceAppConfiguration;
    private final ObjectMapper objectMapper;
    private final HttpClientFactory httpClientFactory;

    public BigCommerceClient(BigCommerceApplicationConfiguration bigCommerceAppConfiguration, ObjectMapper objectMapper, HttpClientFactory httpClientFactory) {
        this.bigCommerceAppConfiguration = bigCommerceAppConfiguration;
        this.objectMapper = objectMapper;
        this.httpClientFactory = httpClientFactory;
    }

    @Override
    public AuthToken authenticate(String code, String storeHash, List<String> requiredScopes) {
        String bodyAsString = getRequestBody(code, requiredScopes, storeHash);
        HttpRequest request = getHttpRequest(bodyAsString);

        return getAuthToken(request);
    }

    private AuthToken getAuthToken(HttpRequest request) {
        try {
            HttpResponse<String> response = getHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            log.debug("status: " + response.statusCode());
            log.debug("received: " + response.body());

            if (response.statusCode() < 200 || response.statusCode() > 399) {
                throw new BigCommerceAppException("invalid big commerce authentication request: " + response.body());
            }

            return objectMapper.readValue(response.body(), AuthToken.class);
        } catch (IOException | InterruptedException e) {
            throw new BigCommerceAppException("failed to process BigCommerce registration request", e);
        }
    }

    private HttpClient getHttpClient() {
        val connectionTimeout = bigCommerceAppConfiguration.getHttpClientConnectTimeout();

        return httpClientFactory.createFor(b -> b.connectTimeout(Duration.of(connectionTimeout, ChronoUnit.MILLIS)));
    }

    private HttpRequest getHttpRequest(String bodyAsString) {
        try {
            return HttpRequest
                    .newBuilder()
                    .uri(bigCommerceAppConfiguration.getBigCommerceUri())
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                    .timeout(Duration.of(bigCommerceAppConfiguration.getHttpClientReadTimeout(), ChronoUnit.MILLIS))
                    .POST(HttpRequest.BodyPublishers.ofString(bodyAsString))
                    .build();
        } catch (URISyntaxException e) {
            throw new BigCommerceAppException("failed to process BigCommerce registration request", e);
        }
    }

    private String getRequestBody(String code, List<String> scopes, String storeHash) {
        val storeConfiguration = bigCommerceAppConfiguration.getStoreCredentialsFor(storeHash);

        val bodyAsString = "client_id=" +
                storeConfiguration.getClientId() +
                "&client_secret=" +
                storeConfiguration.getClientSecret() +
                "&code=" +
                code +
                "&scope=" +
                String.join(" ", scopes) +
                "&grant_type=authorization_code" +
                "&redirect_uri=" +
                bigCommerceAppConfiguration.getRedirectUri() +
                "&context=stores/" +
                storeHash;

        log.debug("auth request sent with: " + bodyAsString);

        return bodyAsString;
    }
}
