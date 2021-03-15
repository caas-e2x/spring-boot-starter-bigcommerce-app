package com.e2x.bigcommerce.app.starter.integrations

import com.e2x.bigcommerce.app.starter.BigCommerceAppException
import com.e2x.bigcommerce.app.starter.HttpClientFactory
import com.e2x.bigcommerce.app.starter.configuration.BigCommerceAppConfiguration
import com.e2x.bigcommerce.app.starter.configuration.HttpClientConfiguration
import com.e2x.bigcommerce.app.starter.configuration.StoreCredentials
import com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Specification

import java.net.http.HttpClient
import java.net.http.HttpResponse

class BigCommerceClientSpecification extends Specification {

    BigCommerceClient testObj
    BigCommerceAppConfiguration configuration = new BigCommerceAppConfiguration()
    HttpClientFactory httpClientFactoryMock = Mock()

    void setup() {
        configuration.url = "http://localhost:8111/oauth/token"
        configuration.storeCredentials = ["store-hash": new StoreCredentials()]
        configuration.httpClient = new HttpClientConfiguration()

        ObjectMapper objectMapper = new ObjectMapper()

        testObj = new BigCommerceClient(configuration, objectMapper, httpClientFactoryMock)
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
        testObj.authenticate("a-code", "store-hash", ["scope1", "scope2"])

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
        testObj.authenticate("a-code", "store-hash", ["scope1", "scope2"])

        then:
        noExceptionThrown()

        where:
        statusCode << (200..399)
    }
}
