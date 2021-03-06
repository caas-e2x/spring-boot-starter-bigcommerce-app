package com.caas.spring.boot.starter.bigcommerce.app;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SignedPayload {
    private User user;
    private Owner owner;
    private String context;
    @JsonProperty("store_hash")
    private String storeHash;
    @JsonIgnore
    private LocalDateTime timestamp;
}
