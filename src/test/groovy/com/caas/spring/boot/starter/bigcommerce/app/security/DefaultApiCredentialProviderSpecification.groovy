package com.caas.spring.boot.starter.bigcommerce.app.security

import com.caas.spring.boot.starter.bigcommerce.app.AuthTokenRepository
import com.caas.spring.boot.starter.bigcommerce.app.BigCommerceApplicationConfiguration
import com.caas.spring.boot.starter.bigcommerce.app.BigCommerceApplicationType
import com.caas.spring.boot.starter.bigcommerce.app.StoreCredentials
import com.caas.spring.boot.starter.bigcommerce.app.model.AuthToken
import spock.lang.Specification

class DefaultApiCredentialProviderSpecification extends Specification {

    DefaultApiCredentialProvider testObj
    AuthTokenRepository authTokenRepository = Mock()

    void "returns api credentials from auth token repository for application application type"() {
        given:
        def config = getConfiguration(BigCommerceApplicationType.APP)

        and:
        testObj = new DefaultApiCredentialProvider(config, authTokenRepository)

        and:
        def authToken = new AuthToken(accessToken: "token")

        and:
        authTokenRepository.getBy("store1") >> authToken

        when:
        def credential = testObj.getFor("store1")

        then:
        credential.clientId == 'client-id'
        credential.clientSecret == 'client-secret'
        credential.credentials == 'token'
    }

    void "returns api credentials from config for connector application type"() {
        given:
        def config = getConfiguration(BigCommerceApplicationType.CONNECTOR)

        and:
        testObj = new DefaultApiCredentialProvider(config, authTokenRepository)

        when:
        def credential = testObj.getFor("store1")

        then:
        credential.clientId == 'client-id'
        credential.clientSecret == 'client-secret'
        credential.credentials == 'key'

        and:
        0 * authTokenRepository.getBy("store1")
    }

    private static BigCommerceApplicationConfiguration getConfiguration(BigCommerceApplicationType appType = BigCommerceApplicationType.APP) {
        new BigCommerceApplicationConfiguration(
                applicationType: appType,
                storeCredentials: [
                        store1: new StoreCredentials(key: 'key', clientSecret: 'client-secret', clientId: 'client-id')
                ]
        )
    }
}
