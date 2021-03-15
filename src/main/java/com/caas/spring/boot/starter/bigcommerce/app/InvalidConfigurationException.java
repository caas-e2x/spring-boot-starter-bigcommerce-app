package com.caas.spring.boot.starter.bigcommerce.app;

public class InvalidConfigurationException extends BigCommerceAppException {
    public InvalidConfigurationException(String message) {
        super(message);
    }

    public InvalidConfigurationException(String message, Exception exception) {
        super(message, exception);
    }
}
