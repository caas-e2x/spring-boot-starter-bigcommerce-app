package com.caas.spring.boot.starter.bigcommerce.app.integrations

import com.caas.spring.boot.starter.bigcommerce.app.BigCommerceAppException
import com.caas.spring.boot.starter.bigcommerce.app.configuration.BigCommerceApplicationConfiguration
import com.caas.spring.boot.starter.bigcommerce.app.configuration.HttpClientConfiguration
import com.caas.spring.boot.starter.bigcommerce.app.HttpClientFactory
import com.caas.spring.boot.starter.bigcommerce.app.configuration.StoreCredentials
import com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Specification

import java.net.http.HttpClient
import java.net.http.HttpResponse

class BigCommerceAuthorizationFlowClientSpecification extends Specification {

    BigCommerceAuthorizationFlowClient testObj
    BigCommerceApplicationConfiguration configuration = new BigCommerceApplicationConfiguration()
    HttpClientFactory httpClientFactoryMock = Mock()

    void setup() {
        configuration.url = "http://localhost:8111/oauth/token"
        configuration.storeCredentials = ["store-hash": new StoreCredentials()]
        configuration.httpClient = new HttpClientConfiguration()

        ObjectMapper objectMapper = new ObjectMapper()

        testObj = new BigCommerceAuthorizationFlowClient(configuration, objectMapper, httpClientFactoryMock)
    }

    void "it should throw an exception if status code is #statusCode"() {
        given:
        HttpResponse<String> httpResponse = Mock()
        httpResponse.statusCode() >> statusCode

        and:
        HttpClient httpClient = Mock()
        httpClient.send(_, _) >> httpResponse

        and:
        httpClientFactoryMock.createFor(_) >> httpClient

        when:
        testObj.fetchFor("a-code", "store-hash", ["scope1", "scope2"])

        then:
        thrown(BigCommerceAppException)

        where:
        statusCode << [(100..199), (400..599)].flatten()
    }

    void "it should not throw an exception if status code is #statusCode"() {
        given:
        HttpResponse<String> httpResponse = Mock()
        httpResponse.statusCode() >> statusCode
        httpResponse.body() >> "{}"

        and:
        HttpClient httpClient = Mock()
        httpClient.send(_, _) >> httpResponse

        and:
        httpClientFactoryMock.createFor(_) >> httpClient

        when:
        testObj.fetchFor("a-code", "store-hash", ["scope1", "scope2"])

        then:
        noExceptionThrown()

        where:
        statusCode << (200..399)
    }
}
