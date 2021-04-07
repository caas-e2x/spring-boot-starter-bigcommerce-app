package com.caas.spring.boot.starter.bigcommerce.app;

import java.util.List;

public interface TokenService<T> {

    T fetchFor(String code, String storeHash, List<String> requiredScopes);

}
