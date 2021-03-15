package com.caas.spring.boot.starter.bigcommerce.app.security.model;

import com.caas.spring.boot.starter.bigcommerce.app.ApiCredentialType;

public class ApiKeyCredential extends ApiCredential {
    private final String apiKey;

    public ApiKeyCredential(String apiKey, String clientSecret, String clientId) {
        super(ApiCredentialType.API_KEY, clientSecret, clientId);

        this.apiKey = apiKey;
    }

    @Override
    public String getCredentials() {
        return apiKey;
    }
}
