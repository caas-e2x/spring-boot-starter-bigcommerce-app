package com.caas.spring.boot.starter.bigcommerce.app.integrations;

import com.caas.spring.boot.starter.bigcommerce.app.TokenService;
import com.caas.spring.boot.starter.bigcommerce.app.BigCommerceAppException;
import com.caas.spring.boot.starter.bigcommerce.app.configuration.BigCommerceApplicationConfiguration;
import com.caas.spring.boot.starter.bigcommerce.app.HttpClientFactory;
import com.caas.spring.boot.starter.bigcommerce.app.AuthToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
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
public class BigCommerceAuthorizationFlowClient implements TokenService<AuthToken> {

    private final BigCommerceApplicationConfiguration configuration;
    private final ObjectMapper objectMapper;
    private final HttpClientFactory httpClientFactory;

    public BigCommerceAuthorizationFlowClient(BigCommerceApplicationConfiguration bigCommerceAppConfiguration, ObjectMapper objectMapper, HttpClientFactory httpClientFactory) {
        this.configuration = bigCommerceAppConfiguration;
        this.objectMapper = objectMapper;
        this.httpClientFactory = httpClientFactory;
    }

    @Override
    public AuthToken fetchFor(String code, String storeHash, List<String> requiredScopes) {
        String bodyAsString = getRequestBody(code, requiredScopes, storeHash);
        HttpRequest request = getHttpRequest(bodyAsString);

        return getAuthToken(request);
    }

    private AuthToken getAuthToken(HttpRequest request) {
        try {
            HttpResponse<String> response = getHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            if (log.isDebugEnabled()) {
                log.debug("status: " + response.statusCode());
                log.debug("received: " + response.body());
            }

            if (response.statusCode() < 200 || response.statusCode() > 399) {
                throw new BigCommerceAppException("invalid big commerce authentication request: " + response.body());
            }

            return objectMapper.readValue(response.body(), AuthToken.class);
        } catch (IOException | InterruptedException e) {
            throw new BigCommerceAppException("failed to process BigCommerce registration request", e);
        }
    }

    private HttpClient getHttpClient() {
        val connectionTimeout = configuration.getHttpClientConnectTimeout();

        return httpClientFactory.createFor(b -> b.connectTimeout(Duration.of(connectionTimeout, ChronoUnit.MILLIS)));
    }

    private HttpRequest getHttpRequest(String bodyAsString) {
        try {
            return HttpRequest
                    .newBuilder()
                    .uri(configuration.getBigCommerceUri())
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                    .timeout(Duration.of(configuration.getHttpClientReadTimeout(), ChronoUnit.MILLIS))
                    .POST(HttpRequest.BodyPublishers.ofString(bodyAsString))
                    .build();

        } catch (URISyntaxException e) {
            throw new BigCommerceAppException("failed to process BigCommerce registration request", e);
        }
    }

    private String getRequestBody(String code, List<String> scopes, String storeHash) {
        val storeConfiguration = configuration.getStoreCredentialsFor(storeHash);

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
                configuration.getRedirectUri() +
                "&context=stores/" +
                storeHash;

        if (log.isDebugEnabled()) {
            log.debug("auth request sent with: " + stripSecretFrom(bodyAsString, storeConfiguration.getClientSecret()));
        }

        return bodyAsString;
    }

    private String stripSecretFrom(String value, String valueToStrip) {
        if (Strings.isNullOrEmpty(value) || Strings.isNullOrEmpty(valueToStrip)) {
            return value;
        }

        return value.replace(valueToStrip, "***");
    }
}
