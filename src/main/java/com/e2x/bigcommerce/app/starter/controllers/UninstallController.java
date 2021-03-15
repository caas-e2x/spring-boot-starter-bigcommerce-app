package com.e2x.bigcommerce.app.starter.controllers;

import com.e2x.bigcommerce.app.starter.AuthTokenRepository;
import com.e2x.bigcommerce.app.starter.SignedPayloadReader;
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
