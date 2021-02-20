package com.caas.spring.boot.starter.bigcommerce.app

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@ActiveProfiles('config')
@SpringBootTest(classes = BigCommerceApplicationAutoConfiguration)
class BigCommerceApplicationAutoConfigurationSpecification extends Specification {

    @Autowired
    ApplicationContext context

    void 'spring context loads with spring boot starter bigcommerce app auto configuration'() {
        expect:
        context
    }

    void 'configuration for spring boot starter bigcommerce app loads'() {
        given:
        def configuration = context.getBean(BigCommerceApplicationConfiguration)

        expect:
        configuration

        and:
        configuration.name
    }
}
