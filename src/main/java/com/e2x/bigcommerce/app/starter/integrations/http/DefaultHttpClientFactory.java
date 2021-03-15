package com.e2x.bigcommerce.app.starter.integrations.http;

import com.e2x.bigcommerce.app.starter.HttpClientFactory;

import java.net.http.HttpClient;
import java.util.function.Consumer;

public class DefaultHttpClientFactory implements HttpClientFactory {

    public HttpClient createFor(Consumer<HttpClient.Builder> builderConsumer) {
        HttpClient.Builder builder = HttpClient.newBuilder();
        builderConsumer.accept(builder);

        return builder.build();
    }
}
