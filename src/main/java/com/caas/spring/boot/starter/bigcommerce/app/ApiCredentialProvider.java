package com.caas.spring.boot.starter.bigcommerce.app;

import com.caas.spring.boot.starter.bigcommerce.app.security.model.ApiCredential;
import org.springframework.lang.NonNull;

public interface ApiCredentialProvider {
    ApiCredential getFor(@NonNull String storeHash);
    ApiCredential getFor(@NonNull String storeHash, @NonNull BigCommerceApplicationType applicationType);
}
