package com.caas.spring.boot.starter.bigcommerce.app.controllers;

import com.caas.spring.boot.starter.bigcommerce.app.AuthTokenRepository;
import com.caas.spring.boot.starter.bigcommerce.app.SignedPayloadReader;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

public class UninstallController {
    private final SignedPayloadReader signedPayloadReader;
    private final AuthTokenRepository authTokenRepository;

    public UninstallController(SignedPayloadReader signedPayloadReader, AuthTokenRepository authTokenRepository) {
        this.signedPayloadReader = signedPayloadReader;
        this.authTokenRepository = authTokenRepository;
    }

    public ResponseEntity<?> uninstall(@RequestParam(name = "signed_payload") String signedPayload) {
        val payload = signedPayloadReader.read(signedPayload);

        authTokenRepository.deleteAllFor(payload.getStoreHash());

        return ResponseEntity.ok().build();
    }
}
