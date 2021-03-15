package com.caas.spring.boot.starter.bigcommerce.app.integrations.http;

import com.caas.spring.boot.starter.bigcommerce.app.HttpClientFactory;

import java.net.http.HttpClient;
import java.util.function.Consumer;

public class DefaultHttpClientFactory implements HttpClientFactory {

    public HttpClient createFor(Consumer<HttpClient.Builder> builderConsumer) {
        HttpClient.Builder builder = HttpClient.newBuilder();
        builderConsumer.accept(builder);

        return builder.build();
    }
}
