package com.caas.spring.boot.starter.bigcommerce.app;

public interface TokenRepository<T> {
    void save(String id, T token);
    T getBy(String id);
    void deleteFor(String id);
}
