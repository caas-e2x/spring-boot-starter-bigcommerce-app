package com.e2x.bigcommerce.app.starter.controllers;

import com.e2x.bigcommerce.app.starter.SignedPayloadReader;
import com.e2x.bigcommerce.app.starter.configuration.BigCommerceAppConfiguration;
import org.springframework.web.bind.annotation.RequestParam;

public class LoadController {

    private final SignedPayloadReader signedPayloadReader;
    private final BigCommerceAppConfiguration configuration;

    public LoadController(SignedPayloadReader signedPayloadReader, BigCommerceAppConfiguration configuration) {
        this.signedPayloadReader = signedPayloadReader;
        this.configuration = configuration;
    }

    public String load(@RequestParam(name = "signed_payload") String signedPayload) {
        signedPayloadReader.read(signedPayload);

        return configuration.getLoadedHtml();
    }
}
