package com.e2x.bigcommerce.app.starter.security.model;

import com.e2x.bigcommerce.app.starter.ApiCredentialType;
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
