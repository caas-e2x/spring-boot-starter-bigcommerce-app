package com.caas.spring.boot.starter.bigcommerce.app

import org.springframework.boot.autoconfigure.AutoConfigurations
import org.springframework.boot.test.context.runner.ApplicationContextRunner
import spock.lang.Specification

class BigCommerceApplicationAutoConfigurationSpecification extends Specification {

    ApplicationContextRunner testObj

    void setup() {
        testObj = new ApplicationContextRunner().withConfiguration(AutoConfigurations.of(BigCommerceApplicationAutoConfiguration))
    }

    void 'verify context loads with spring boot starter bigcommerce app auto configuration'() {
        expect:
        testObj
    }
}
