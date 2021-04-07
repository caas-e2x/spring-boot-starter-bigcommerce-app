package com.caas.spring.boot.starter.bigcommerce.app.controllers;

import com.caas.spring.boot.starter.bigcommerce.app.configuration.BigCommerceApplicationConfiguration;
import com.caas.spring.boot.starter.bigcommerce.app.SignedPayloadReader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller to handle load callback from BigCommerce
 *
 * @see <a href="https://developer.bigcommerce.com/api-docs/apps/guide/callbacks">Single-Click App Callbacks</a>
 */
public class LoadController {

    private final SignedPayloadReader signedPayloadReader;
    private final BigCommerceApplicationConfiguration configuration;

    public LoadController(SignedPayloadReader signedPayloadReader, BigCommerceApplicationConfiguration configuration) {
        this.signedPayloadReader = signedPayloadReader;
        this.configuration = configuration;
    }

    public String load(@RequestParam(name = "signed_payload") String signedPayload) {
        signedPayloadReader.read(signedPayload);

        return configuration.getLoadedHtml();
    }
}
