package com.caas.spring.boot.starter.bigcommerce.app.security.model;

import com.caas.spring.boot.starter.bigcommerce.app.ApiCredentialType;

public class ApiTokenCredential extends ApiCredential {
    private final String token;

    public ApiTokenCredential(String token, String clientSecret, String clientId) {
        super(ApiCredentialType.TOKEN, clientSecret, clientId);

        this.token = token;
    }

    @Override
    public String getCredentials() {
        return token;
    }
}
