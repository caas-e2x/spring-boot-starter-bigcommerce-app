package com.caas.spring.boot.starter.bigcommerce.app

import com.caas.spring.boot.starter.bigcommerce.app.configuration.BigCommerceApplicationConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@ActiveProfiles('test')
@SpringBootTest(classes = BigCommerceApplicationStarterAppConfiguration)
class BigCommerceApplicationAutoConfigurationSpecification extends Specification {

    @Autowired
    ApplicationContext context

    void 'spring context loads with spring boot starter bigcommerce app auto configuration'() {
        expect:
        context
    }

    void 'configuration for spring boot starter bigcommerce app loads'() {
        given:
        BigCommerceApplicationConfiguration configuration = context.getBean(BigCommerceApplicationConfiguration)

        expect:
        configuration

        and:
        configuration.url == 'https://a/login/url'
        configuration.requiredScopes == ['scope-1', 'scope-2']
        configuration.redirectUri == 'https:/a/redirect/uri'

        and:
        configuration.storeCredentials
        configuration.storeCredentials['storeid']
        configuration.storeCredentials['storeid'].key == 'a-store-key'
        configuration.storeCredentials['storeid'].clientId == 'a-client-id'
        configuration.storeCredentials['storeid'].clientSecret == 'a-client-secret'

        and:
        configuration.httpClient.readTimeout == 10000
        configuration.httpClient.connectTimeout == 1000

        and:
        configuration.authenticatedHtml == '/path/to/html'
        configuration.loadedHtml == '/path/to/html'
    }
}
