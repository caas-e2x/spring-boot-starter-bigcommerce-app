package com.caas.spring.boot.starter.bigcommerce.app.security.model;

import com.caas.spring.boot.starter.bigcommerce.app.ApiCredentialType;
import lombok.Getter;

@Getter
public abstract class ApiCredential {
    private final ApiCredentialType apiCredentialType;
    private final String clientSecret;
    private final String clientId;

    protected ApiCredential(ApiCredentialType apiCredentialType, String clientSecret, String clientId) {
        this.apiCredentialType = apiCredentialType;
        this.clientSecret = clientSecret;
        this.clientId = clientId;
    }

    public abstract String getCredentials();

}
