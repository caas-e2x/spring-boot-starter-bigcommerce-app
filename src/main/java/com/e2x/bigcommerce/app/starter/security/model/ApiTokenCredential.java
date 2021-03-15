package com.e2x.bigcommerce.app.starter.security.model;

import com.e2x.bigcommerce.app.starter.ApiCredentialType;

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
