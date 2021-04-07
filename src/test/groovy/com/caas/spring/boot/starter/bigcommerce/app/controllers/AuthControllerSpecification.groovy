package com.caas.spring.boot.starter.bigcommerce.app.controllers

import com.caas.spring.boot.starter.bigcommerce.app.AuthToken
import com.caas.spring.boot.starter.bigcommerce.app.TokenRepository
import com.caas.spring.boot.starter.bigcommerce.app.TokenService
import com.caas.spring.boot.starter.bigcommerce.app.BigCommerceAppException
import com.caas.spring.boot.starter.bigcommerce.app.configuration.BigCommerceApplicationConfiguration
import spock.lang.Specification

class AuthControllerSpecification extends Specification {

    AuthController testObj
    TokenService authorizationFlowController = Mock()
    TokenRepository authTokenStorage = Mock()
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
        authorizationFlowController.fetchFor(*_) >> token

        when:
        testObj.authenticate("code", "a b", "a-store")

        then:
        noExceptionThrown()

        and:
        1 * authTokenStorage.save("a-store", token)
    }
}
