package com.e2x.bigcommerce.app.starter.controllers

import com.e2x.bigcommerce.app.starter.AuthTokenRepository
import com.e2x.bigcommerce.app.starter.AuthorizationFlowController
import com.e2x.bigcommerce.app.starter.BigCommerceAppException
import com.e2x.bigcommerce.app.starter.configuration.BigCommerceAppConfiguration
import com.e2x.bigcommerce.app.starter.model.AuthToken
import spock.lang.Specification

class AuthControllerSpecification extends Specification {

    AuthController testObj
    AuthorizationFlowController authorizationFlowController = Mock()
    AuthTokenRepository authTokenStorage = Mock()
    BigCommerceAppConfiguration configuration = Mock()

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
