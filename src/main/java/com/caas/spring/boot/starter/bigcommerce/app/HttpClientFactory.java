package com.caas.spring.boot.starter.bigcommerce.app;

import java.net.http.HttpClient;
import java.util.function.Consumer;

public interface HttpClientFactory {
    HttpClient createFor(Consumer<HttpClient.Builder> builderConsumer);
}
