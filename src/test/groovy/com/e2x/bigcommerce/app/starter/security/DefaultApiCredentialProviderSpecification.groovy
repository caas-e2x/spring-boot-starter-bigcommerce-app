package com.e2x.bigcommerce.app.starter.security

import com.e2x.bigcommerce.app.starter.AuthTokenRepository
import com.e2x.bigcommerce.app.starter.configuration.BigCommerceAppConfiguration
import com.e2x.bigcommerce.app.starter.configuration.BigCommerceApplicationType
import com.e2x.bigcommerce.app.starter.configuration.StoreCredentials
import com.e2x.bigcommerce.app.starter.model.AuthToken
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

    private static BigCommerceAppConfiguration getConfiguration(BigCommerceApplicationType appType = BigCommerceApplicationType.APP) {
        new BigCommerceAppConfiguration(
                applicationType: appType,
                storeCredentials: [
                        store1: new StoreCredentials(key: 'key', clientSecret: 'client-secret', clientId: 'client-id')
                ]
        )
    }
}
