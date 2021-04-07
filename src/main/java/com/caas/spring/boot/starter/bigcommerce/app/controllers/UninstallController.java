package com.caas.spring.boot.starter.bigcommerce.app.controllers;

import com.caas.spring.boot.starter.bigcommerce.app.TokenRepository;
import com.caas.spring.boot.starter.bigcommerce.app.SignedPayloadReader;
import com.caas.spring.boot.starter.bigcommerce.app.AuthToken;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller to handle uninstall callback from BigCommerce
 *
 * @see <a href="https://developer.bigcommerce.com/api-docs/apps/guide/callbacks">Single-Click App Callbacks</a>
 */
public class UninstallController {
    private final SignedPayloadReader signedPayloadReader;
    private final TokenRepository<AuthToken> tokenRepository;

    public UninstallController(SignedPayloadReader signedPayloadReader, TokenRepository<AuthToken> tokenRepository) {
        this.signedPayloadReader = signedPayloadReader;
        this.tokenRepository = tokenRepository;
    }

    public ResponseEntity<?> uninstall(@RequestParam(name = "signed_payload") String signedPayload) {
        val payload = signedPayloadReader.read(signedPayload);

        tokenRepository.deleteFor(payload.getStoreHash());

        return ResponseEntity.ok().build();
    }
}
