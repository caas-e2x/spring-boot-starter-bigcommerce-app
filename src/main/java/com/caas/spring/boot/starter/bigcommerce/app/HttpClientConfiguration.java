package com.caas.spring.boot.starter.bigcommerce.app;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HttpClientConfiguration {
    private int readTimeout = 10000;
    private int connectTimeout = 10000;
}
