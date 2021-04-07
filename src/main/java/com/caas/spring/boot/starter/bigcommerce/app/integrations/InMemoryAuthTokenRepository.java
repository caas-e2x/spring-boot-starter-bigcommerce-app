package com.caas.spring.boot.starter.bigcommerce.app.integrations;

import com.caas.spring.boot.starter.bigcommerce.app.TokenRepository;
import com.caas.spring.boot.starter.bigcommerce.app.AuthToken;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryAuthTokenRepository implements TokenRepository<AuthToken> {
    private final Map<String, AuthToken> authTokenMap = new ConcurrentHashMap<>();

    @Override
    public void save(String id, AuthToken token) {
        authTokenMap.put(id, token);
    }

    @Override
    public AuthToken getBy(String id) {
        return authTokenMap.get(id);
    }

    @Override
    public void deleteFor(String id) {
        authTokenMap.remove(id);
    }
}
