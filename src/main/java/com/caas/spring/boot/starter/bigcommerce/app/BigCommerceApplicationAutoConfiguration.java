package com.caas.spring.boot.starter.bigcommerce.app;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(BigCommerceApplicationConfiguration.class)
public class BigCommerceApplicationAutoConfiguration {
}
