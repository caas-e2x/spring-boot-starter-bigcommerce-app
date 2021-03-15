package com.caas.spring.boot.starter.bigcommerce.app;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@ConfigurationProperties(prefix="caas.bigcommerce")
public class BigCommerceApplicationConfiguration {
    private BigCommerceApplicationType applicationType = BigCommerceApplicationType.APP;

    private String url = "https://login.bigcommerce.com/oauth2/token";
    private List<String> requiredScopes;
    private String redirectUri;

    private Map<String, StoreCredentials> storeCredentials;

    private HttpClientConfiguration httpClient;
    private HtmlConfiguration html;

    public URI getBigCommerceUri() throws URISyntaxException {
        return new URI(url);
    }

    public String getAuthenticatedHtml() {
        return getHtml().getAuthenticatedHtml();
    }

    public String getLoadedHtml() {
        return getHtml().getLoadedHtml();
    }

    public StoreCredentials getStoreCredentialsFor(String storeHash) {
        return getStoreCredentials().get(storeHash);
    }

    public int getHttpClientReadTimeout() {
        return httpClient.getReadTimeout();
    }

    public int getHttpClientConnectTimeout() {
        return httpClient.getConnectTimeout();
    }
}
