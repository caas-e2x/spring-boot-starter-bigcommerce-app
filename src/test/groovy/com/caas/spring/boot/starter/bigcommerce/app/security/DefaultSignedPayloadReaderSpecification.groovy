package com.caas.spring.boot.starter.bigcommerce.app.security

import com.caas.spring.boot.starter.bigcommerce.app.BigCommerceAppException
import com.caas.spring.boot.starter.bigcommerce.app.Owner
import com.caas.spring.boot.starter.bigcommerce.app.SignedPayload
import com.caas.spring.boot.starter.bigcommerce.app.User
import com.caas.spring.boot.starter.bigcommerce.app.configuration.BigCommerceApplicationConfiguration
import com.caas.spring.boot.starter.bigcommerce.app.configuration.StoreCredentials
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.base.Charsets
import spock.lang.Specification

class DefaultSignedPayloadReaderSpecification extends Specification {
    private static String STORE_HASH_KEY = 'z4zn3wo'
    private static String SECRET_KEY = 'a-secret-key'

    DefaultSignedPayloadReader testObj
    BigCommerceApplicationConfiguration configuration
    ObjectMapper objectMapper = new ObjectMapper()

    void setup() {

        configuration = new BigCommerceApplicationConfiguration()

        testObj = new DefaultSignedPayloadReader(configuration, objectMapper)
    }

    void "it should return the payload when the signature matches the payload given the secret key"() {
        given:
        configuration.setStoreCredentials([(STORE_HASH_KEY): new StoreCredentials(clientSecret: SECRET_KEY)])

        and:
        def encodedPayload = encode(objectMapper.writeValueAsString(signedPayload))

        when:
        def result = testObj.read("${encodedPayload}.M2M5YTE5NmFiNTY5N2JiMDAxOGM1MjI5YWI4NmFmMmIzYjM5NjBkODQ0ODM0N2Q5ODBhMGRjZDQ2ZTIwY2U4OA==")

        then:
        result

        and:
        result == signedPayload
    }

    void "it should throw an exception if the payload does not match the signature"() {
        given:
        configuration.setStoreCredentials([(STORE_HASH_KEY): new StoreCredentials(clientSecret: "a-different-key")])
        def encodedPayload = encode(objectMapper.writeValueAsString(signedPayload))

        when:
        testObj.read("${encodedPayload}.${hmacSignature}")

        then:
        thrown(PayloadSignatureException)
    }

    void "it should throw an exception when the payload does not match the signature"() {
        given:
        configuration.setStoreCredentials([(STORE_HASH_KEY): new StoreCredentials(clientSecret: SECRET_KEY)])
        def encodedPayload = encode(objectMapper.writeValueAsString(signedPayload))

        when:
        testObj.read("${encodedPayload}.OWE2MjBjYTAxMjEwYzY3MGM2NWNlMTgxZGI1ZGMyOWYyNmY3M2JlOTY4ZDRkYjVlZDRhMWVhOTcxYTUzMzQxNA==")

        then:
        thrown(PayloadSignatureException)
    }

    void "it should throw an error if the store cannot be found"() {
        configuration.setStoreCredentials(["other-store": new StoreCredentials(clientSecret: "a-secret-key")])
        def encodedPayload = encode(objectMapper.writeValueAsString(signedPayload))

        when:
        testObj.read("${encodedPayload}.${hmacSignature}")

        then:
        thrown(BigCommerceAppException)
    }

    private static String encode(String payload) {
        Base64.encoder.encodeToString(payload.getBytes(Charsets.UTF_8))
    }

    private SignedPayload signedPayload = new SignedPayload(
            user: new User(id: 9128, email: "user@mybigcommerce.com"),
            owner: new Owner(id: 9128, email: "user@mybigcommerce.com"),
            context: "stores/z4zn3wo",
            storeHash: STORE_HASH_KEY,
            timestamp: null
    )

    private static String getHmacSignature() {
        "M2M5YTE5NmFiNTY5N2JiMDAxOGM1MjI5YWI4NmFmMmIzYjM5NjBkODQ0ODM0N2Q5ODBhMGRjZDQ2ZTIwY2U4OA=="
    }
}
