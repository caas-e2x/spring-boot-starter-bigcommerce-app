package com.caas.spring.boot.starter.bigcommerce.app.controllers

import com.caas.spring.boot.starter.bigcommerce.app.BigCommerceApplicationConfiguration
import com.caas.spring.boot.starter.bigcommerce.app.SignedPayloadReader
import spock.lang.Specification

class LoadControllerSpecification extends Specification {

    LoadController testObj
    BigCommerceApplicationConfiguration configuration = Mock()
    SignedPayloadReader payloadReader = Mock()

    void setup() {
        testObj = new LoadController(payloadReader, configuration)
    }

    void "it should return expected html invoked"() {
        given:
        def expectedHtml = "some-html"

        and:
        configuration.getLoadedHtml() >> expectedHtml

        when:
        def result = testObj.load("a-payload")

        then:
        result == expectedHtml
    }
}
