package com.caas.spring.boot.starter.bigcommerce.app;

import com.caas.spring.boot.starter.bigcommerce.app.model.AuthToken;

/**
 * Implementation of this class must provide the capability to store and retrieve authentication token provided by BigCommerce.
 */
public interface AuthTokenRepository {
    void save(String storeHash, AuthToken authToken);
    AuthToken getBy(String storeHash);
    void deleteAllFor(String storeHash);
}
