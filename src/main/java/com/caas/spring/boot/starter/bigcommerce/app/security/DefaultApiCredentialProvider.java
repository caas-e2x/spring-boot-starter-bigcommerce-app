package com.caas.spring.boot.starter.bigcommerce.app.security;

import com.caas.spring.boot.starter.bigcommerce.app.*;
import com.caas.spring.boot.starter.bigcommerce.app.security.model.ApiCredential;
import com.caas.spring.boot.starter.bigcommerce.app.security.model.ApiKeyCredential;
import com.caas.spring.boot.starter.bigcommerce.app.security.model.ApiTokenCredential;
import lombok.val;
import org.springframework.lang.NonNull;

public class DefaultApiCredentialProvider implements ApiCredentialProvider {

    private final BigCommerceApplicationConfiguration bigCommerceAppConfiguration;
    private final AuthTokenRepository authTokenRepository;

    public DefaultApiCredentialProvider(BigCommerceApplicationConfiguration bigCommerceAppConfiguration, AuthTokenRepository authTokenRepository) {
        this.bigCommerceAppConfiguration = bigCommerceAppConfiguration;
        this.authTokenRepository = authTokenRepository;
    }

    public ApiCredential getFor(@NonNull String storeHash) {
        return getFor(storeHash, bigCommerceAppConfiguration.getApplicationType());
    }

    public ApiCredential getFor(@NonNull String storeHash, @NonNull BigCommerceApplicationType applicationType) {
        var store = bigCommerceAppConfiguration.getStoreCredentialsFor(storeHash);

        switch (applicationType) {
            case APP:
                val token = authTokenRepository.getBy(storeHash);
                if (token == null) {
                    throw new InvalidConfigurationException("no token found for store " + storeHash);
                }

                return new ApiTokenCredential(token.getAccessToken(), store.getClientSecret(), store.getClientId());
            case CONNECTOR:
                return new ApiKeyCredential(store.getKey(), store.getClientSecret(), store.getClientId());
            default:
                throw new InvalidConfigurationException("missing application-type configuration in your configuration file");
        }
    }

}
