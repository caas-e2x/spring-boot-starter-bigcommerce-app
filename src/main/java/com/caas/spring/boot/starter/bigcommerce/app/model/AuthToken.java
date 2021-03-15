package com.caas.spring.boot.starter.bigcommerce.app.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AuthToken {
    @JsonProperty("access_token")
    private String accessToken;
    private String scope;
    private String context;
    private User user;
}
