package com.e2x.bigcommerce.app.starter.security;

import com.e2x.bigcommerce.app.starter.ApiCredentialProvider;
import com.e2x.bigcommerce.app.starter.AuthTokenRepository;
import com.e2x.bigcommerce.app.starter.configuration.BigCommerceAppConfiguration;
import com.e2x.bigcommerce.app.starter.configuration.BigCommerceApplicationType;
import com.e2x.bigcommerce.app.starter.configuration.InvalidConfigurationException;
import com.e2x.bigcommerce.app.starter.security.model.ApiCredential;
import com.e2x.bigcommerce.app.starter.security.model.ApiKeyCredential;
import com.e2x.bigcommerce.app.starter.security.model.ApiTokenCredential;
import lombok.val;
import org.springframework.lang.NonNull;

public class DefaultApiCredentialProvider implements ApiCredentialProvider {

    private final BigCommerceAppConfiguration bigCommerceAppConfiguration;
    private final AuthTokenRepository authTokenRepository;

    public DefaultApiCredentialProvider(BigCommerceAppConfiguration bigCommerceAppConfiguration, AuthTokenRepository authTokenRepository) {
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
