package com.e2x.bigcommerce.app.starter.controllers

import com.e2x.bigcommerce.app.starter.SignedPayloadReader
import com.e2x.bigcommerce.app.starter.configuration.BigCommerceAppConfiguration
import spock.lang.Specification

class LoadControllerSpecification extends Specification {

    LoadController testObj
    BigCommerceAppConfiguration configuration = Mock()
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

    def 'Should produce result being 2'() {
        given:
        def var1 = 1
        def var2 = 1

        when:
        def result = var1+var2

        then:
        result == 2
    }

}
