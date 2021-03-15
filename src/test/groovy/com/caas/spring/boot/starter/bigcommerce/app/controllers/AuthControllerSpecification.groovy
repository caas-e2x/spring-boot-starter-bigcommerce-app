package com.caas.spring.boot.starter.bigcommerce.app.controllers

import com.caas.spring.boot.starter.bigcommerce.app.AuthTokenRepository
import com.caas.spring.boot.starter.bigcommerce.app.AuthorizationFlowController
import com.caas.spring.boot.starter.bigcommerce.app.BigCommerceAppException
import com.caas.spring.boot.starter.bigcommerce.app.BigCommerceApplicationConfiguration
import com.caas.spring.boot.starter.bigcommerce.app.model.AuthToken
import spock.lang.Specification

class AuthControllerSpecification extends Specification {

    AuthController testObj
    AuthorizationFlowController authorizationFlowController = Mock()
    AuthTokenRepository authTokenStorage = Mock()
    BigCommerceApplicationConfiguration configuration = Mock()

    void setup() {
        testObj = new AuthController(authorizationFlowController, configuration, authTokenStorage)
    }

    void "it should throw exception if there are missing scopes"() {
        given:
        configuration.getRequiredScopes() >> ["a", "b", "c"]

        when:
        testObj.authenticate("code", "a b", "a-store")

        then:
        thrown(BigCommerceAppException)
    }

    void "it should return the configured html path or string"() {
        given:
        configuration.getRequiredScopes() >> ["a", "b"]

        and:
        def expectedHtml = "expected-html"
        configuration.getAuthenticatedHtml() >> expectedHtml

        when:
        def result = testObj.authenticate("code", "a b", "a-store")

        then:
        noExceptionThrown()

        and:
        result == expectedHtml
    }

    void "it should store the auth token returned by big commerce authentication request"() {
        given:
        configuration.getRequiredScopes() >> ["a", "b"]

        and:
        def token = Mock(AuthToken)
        authorizationFlowController.authenticate(*_) >> token

        when:
        testObj.authenticate("code", "a b", "a-store")

        then:
        noExceptionThrown()

        and:
        1 * authTokenStorage.save("a-store", token)
    }
}
