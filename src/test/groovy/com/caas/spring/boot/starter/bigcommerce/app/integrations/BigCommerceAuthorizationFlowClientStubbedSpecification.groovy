package com.caas.spring.boot.starter.bigcommerce.app.integrations

import com.caas.spring.boot.starter.bigcommerce.app.AuthToken
import com.caas.spring.boot.starter.bigcommerce.app.User
import com.caas.spring.boot.starter.bigcommerce.app.configuration.BigCommerceApplicationConfiguration
import com.caas.spring.boot.starter.bigcommerce.app.configuration.HttpClientConfiguration
import com.caas.spring.boot.starter.bigcommerce.app.configuration.StoreCredentials
import com.caas.spring.boot.starter.bigcommerce.app.integrations.http.DefaultHttpClientFactory
import com.fasterxml.jackson.databind.ObjectMapper

import static com.github.tomakehurst.wiremock.client.WireMock.*

class BigCommerceAuthorizationFlowClientStubbedSpecification extends WiremockSpecification {

    BigCommerceAuthorizationFlowClient testObj
    BigCommerceApplicationConfiguration configuration = new BigCommerceApplicationConfiguration()

    void setup() {
        configuration.url = "http://localhost:8111/oauth/token"
        configuration.storeCredentials = ["store-hash": new StoreCredentials()]
        configuration.httpClient = new HttpClientConfiguration()

        ObjectMapper objectMapper = new ObjectMapper()

        testObj = new BigCommerceAuthorizationFlowClient(configuration, objectMapper, new DefaultHttpClientFactory())
    }

    void "when big commerce token endpoint request is successful it should return an auth response with the relevant information"() {
        given:
        def expectedResult = new AuthToken(accessToken: "a-token", user: new User(id: 1, email: "email@email.com"), scope: "scope1,scope2", context: "a-context")

        and:
        stubFor(post(urlEqualTo("/oauth/token"))
                .willReturn(aResponse()
                        .withBody("""
                        {
                          "access_token": "a-token",
                          "scope": "scope1,scope2",
                          "user": {
                            "id": 1,
                            "email": "email@email.com"
                          },
                          "context": "a-context"
                        }""")))

        when:
        def response = testObj.fetchFor("a-code", "store-hash", ["scope1", "scope2"])

        then:
        response == expectedResult
    }

}
