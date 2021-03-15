package com.e2x.bigcommerce.app.starter.integrations;

import com.e2x.bigcommerce.app.starter.AuthTokenRepository;
import com.e2x.bigcommerce.app.starter.model.AuthToken;

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
