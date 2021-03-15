package com.e2x.bigcommerce.app.starter;

import com.e2x.bigcommerce.app.starter.configuration.BigCommerceApplicationType;
import com.e2x.bigcommerce.app.starter.security.model.ApiCredential;
import org.springframework.lang.NonNull;

public interface ApiCredentialProvider {
    ApiCredential getFor(@NonNull String storeHash);
    ApiCredential getFor(@NonNull String storeHash, @NonNull BigCommerceApplicationType applicationType);
}
