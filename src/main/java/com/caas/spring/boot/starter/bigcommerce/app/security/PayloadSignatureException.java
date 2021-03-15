package com.caas.spring.boot.starter.bigcommerce.app.security;

import com.caas.spring.boot.starter.bigcommerce.app.BigCommerceAppException;

public class PayloadSignatureException extends BigCommerceAppException {
    public PayloadSignatureException(String message) {
        super(message);
    }
}
