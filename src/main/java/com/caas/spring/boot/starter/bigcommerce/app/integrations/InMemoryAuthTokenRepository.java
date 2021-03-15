package com.caas.spring.boot.starter.bigcommerce.app.integrations;

import com.caas.spring.boot.starter.bigcommerce.app.AuthTokenRepository;
import com.caas.spring.boot.starter.bigcommerce.app.model.AuthToken;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryAuthTokenRepository implements AuthTokenRepository {
    private final Map<String, AuthToken> authTokenMap = new ConcurrentHashMap<>();

    @Override
    public void save(String storeHash, AuthToken authToken) {
        authTokenMap.put(storeHash, authToken);
    }

    @Override
    public AuthToken getBy(String storeHash) {
        return authTokenMap.get(storeHash);
    }

    @Override
    public void deleteAllFor(String storeHash) {
        authTokenMap.remove(storeHash);
    }
}
