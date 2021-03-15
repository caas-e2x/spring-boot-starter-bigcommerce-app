package com.caas.spring.boot.starter.bigcommerce.app;

public class BigCommerceAppException extends RuntimeException {

    public BigCommerceAppException(String message) {
        this(message, null);
    }

    public BigCommerceAppException(String message, Exception exception) {
        super(message, exception);
    }
}
