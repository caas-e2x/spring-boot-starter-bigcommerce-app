package com.e2x.bigcommerce.app.starter.configuration;

import lombok.Data;

@Data
public class StoreCredentials {
    private String key;
    private String clientId;
    private String clientSecret;
}
