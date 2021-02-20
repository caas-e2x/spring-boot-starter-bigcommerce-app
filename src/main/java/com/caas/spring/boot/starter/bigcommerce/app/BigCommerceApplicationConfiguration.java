package com.caas.spring.boot.starter.bigcommerce.app;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix="caas.bigcommerce")
public class BigCommerceApplicationConfiguration {
    private String name;
}
