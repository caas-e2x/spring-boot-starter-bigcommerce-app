package com.caas.spring.boot.starter.bigcommerce.app.configuration;

import lombok.Data;

@Data
public class StoreCredentials {
    private String key;
    private String clientId;
    private String clientSecret;
}
