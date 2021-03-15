package com.e2x.bigcommerce.app.starter;

import com.e2x.bigcommerce.app.starter.model.AuthToken;

/**
 * Implementation of this class must provide the capability to store and retrieve authentication token provided by BigCommerce.
 */
public interface AuthTokenRepository {
    void save(String storeHash, AuthToken authToken);
    AuthToken getBy(String storeHash);
    void deleteAllFor(String storeHash);
}
