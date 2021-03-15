package com.e2x.bigcommerce.app.starter.security.model;

import com.e2x.bigcommerce.app.starter.ApiCredentialType;

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
