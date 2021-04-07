package com.caas.spring.boot.starter.bigcommerce.app;

public interface SignedPayloadReader {
    SignedPayload read(String signedPayload);
}
