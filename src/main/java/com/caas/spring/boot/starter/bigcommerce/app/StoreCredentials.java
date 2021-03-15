package com.caas.spring.boot.starter.bigcommerce.app;

import lombok.Data;

@Data
public class StoreCredentials {
    private String key;
    private String clientId;
    private String clientSecret;
}
