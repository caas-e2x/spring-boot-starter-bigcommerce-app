package com.e2x.bigcommerce.app.starter;

import java.net.http.HttpClient;
import java.util.function.Consumer;

public interface HttpClientFactory {
    HttpClient createFor(Consumer<HttpClient.Builder> builderConsumer);
}
