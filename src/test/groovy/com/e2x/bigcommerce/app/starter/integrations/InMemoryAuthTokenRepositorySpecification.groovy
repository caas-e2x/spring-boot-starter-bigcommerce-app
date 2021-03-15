package com.e2x.bigcommerce.app.starter.integrations

import com.e2x.bigcommerce.app.starter.model.AuthToken
import spock.lang.Specification

class InMemoryAuthTokenRepositorySpecification extends Specification {

    InMemoryAuthTokenRepository testObj

    void setup() {
        testObj = new InMemoryAuthTokenRepository()
    }

    void "can manage store token"() {
        given:
        def authToken = Mock(AuthToken)
        def storeHash = "a-store-hash"

        when:
        def result = testObj.getBy(storeHash)

        then:
        result == null

        when:
        testObj.save(storeHash, authToken)
        result = testObj.getBy(storeHash)

        then:
        result

        and:
        result == authToken

        when:
        def otherToken = new AuthToken()
        testObj.save("other-store", otherToken)
        result = testObj.getBy(storeHash)

        then:
        result

        and:
        result == authToken

        when:
        result = testObj.getBy("other-store")

        then:
        result

        and:
        result == otherToken

        when:
        testObj.deleteAllFor(storeHash)

        and:
        result = testObj.getBy(storeHash)

        then:
        !result

        when:
        result = testObj.getBy("other-store")

        then:
        result
    }
}
