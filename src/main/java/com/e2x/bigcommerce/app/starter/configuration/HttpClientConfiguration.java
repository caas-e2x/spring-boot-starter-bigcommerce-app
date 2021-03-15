package com.e2x.bigcommerce.app.starter.configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HttpClientConfiguration {
    private int readTimeout = 10000;
    private int connectTimeout = 10000;
}
