package com.caas.spring.boot.starter.bigcommerce.app;

import com.caas.spring.boot.starter.bigcommerce.app.model.SignedPayload;

public interface SignedPayloadReader {
    SignedPayload read(String signedPayload);
}
