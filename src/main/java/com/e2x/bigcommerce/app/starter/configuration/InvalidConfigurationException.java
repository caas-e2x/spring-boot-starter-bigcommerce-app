package com.e2x.bigcommerce.app.starter.configuration;

import com.e2x.bigcommerce.app.starter.BigCommerceAppException;

public class InvalidConfigurationException extends BigCommerceAppException {
    public InvalidConfigurationException(String message) {
        super(message);
    }

    public InvalidConfigurationException(String message, Exception exception) {
        super(message, exception);
    }
}
