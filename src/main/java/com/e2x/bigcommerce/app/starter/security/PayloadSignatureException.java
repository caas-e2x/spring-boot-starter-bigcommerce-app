package com.e2x.bigcommerce.app.starter.security;

import com.e2x.bigcommerce.app.starter.BigCommerceAppException;

public class PayloadSignatureException extends BigCommerceAppException {
    public PayloadSignatureException(String message) {
        super(message);
    }
}
