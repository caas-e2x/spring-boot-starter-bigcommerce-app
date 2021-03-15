package com.e2x.bigcommerce.app.starter.integrations


import com.e2x.bigcommerce.app.starter.configuration.BigCommerceAppConfiguration
import com.e2x.bigcommerce.app.starter.configuration.HttpClientConfiguration
import com.e2x.bigcommerce.app.starter.configuration.StoreCredentials
import com.e2x.bigcommerce.app.starter.integrations.http.DefaultHttpClientFactory
import com.e2x.bigcommerce.app.starter.model.AuthToken
import com.e2x.bigcommerce.app.starter.model.User
import com.fasterxml.jackson.databind.ObjectMapper

import static com.github.tomakehurst.wiremock.client.WireMock.*

class BigCommerceClientStubbedSpecification extends WiremockSpecification {

    BigCommerceClient testObj
    BigCommerceAppConfiguration configuration = new BigCommerceAppConfiguration()

    void setup() {
        configuration.url = "http://localhost:8111/oauth/token"
        configuration.storeCredentials = ["store-hash": new StoreCredentials()]
        configuration.httpClient = new HttpClientConfiguration()

        ObjectMapper objectMapper = new ObjectMapper()

        testObj = new BigCommerceClient(configuration, objectMapper, new DefaultHttpClientFactory())
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
        def response = testObj.authenticate("a-code", "store-hash", ["scope1", "scope2"])

        then:
        response == expectedResult
    }

}
