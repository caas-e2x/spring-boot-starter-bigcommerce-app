package com.e2x.bigcommerce.app.starter;

import com.e2x.bigcommerce.app.starter.model.SignedPayload;

public interface SignedPayloadReader {
    SignedPayload read(String signedPayload);
}
