package com.caas.spring.boot.starter.bigcommerce.app.security;

import com.caas.spring.boot.starter.bigcommerce.app.BigCommerceAppException;
import com.caas.spring.boot.starter.bigcommerce.app.configuration.BigCommerceApplicationConfiguration;
import com.caas.spring.boot.starter.bigcommerce.app.SignedPayloadReader;
import com.caas.spring.boot.starter.bigcommerce.app.SignedPayload;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import lombok.val;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class DefaultSignedPayloadReader implements SignedPayloadReader {

    private final BigCommerceApplicationConfiguration configuration;
    private final ObjectMapper objectMapper;

    public DefaultSignedPayloadReader(BigCommerceApplicationConfiguration configuration, ObjectMapper objectMapper) {
        this.configuration = configuration;
        this.objectMapper = objectMapper;
    }

    @Override
    public SignedPayload read(String signedPayload) {
        val split = signedPayload.split("\\.");
        val encodedPayload = split[0];
        val encodedHmac = split[1];

        val decoder = Base64.getDecoder();
        val hmacSignature = decoder.decode(encodedHmac);
        val decodedPayload = decoder.decode(encodedPayload);

        SignedPayload payload = deserializeFrom(decodedPayload);
        val result = encrypt(decodedPayload, payload.getStoreHash());

        if (!result.toString().equals(new String(hmacSignature, StandardCharsets.UTF_8))) {
            throw new PayloadSignatureException("invalid payload signature");
        }

        return payload;
    }

    private HashCode encrypt(byte[] decodedPayload, String storeHash) {
        val storeConfiguration = configuration.getStoreCredentialsFor(storeHash);

        if (storeConfiguration == null) {
            throw new BigCommerceAppException("could not find store with hash" + storeHash);
        }

        val clientSecret = configuration.getStoreCredentialsFor(storeHash).getClientSecret();
        val digest = Hashing.hmacSha256(clientSecret.getBytes(StandardCharsets.UTF_8));
        return digest.hashString(new String(decodedPayload, StandardCharsets.UTF_8), StandardCharsets.UTF_8);
    }

    private SignedPayload deserializeFrom(byte[] payload) {
        try {
            return objectMapper.readValue(payload, SignedPayload.class);
        } catch (IOException e) {
            throw new BigCommerceAppException("failed to deserialize payload", e);
        }
    }
}
