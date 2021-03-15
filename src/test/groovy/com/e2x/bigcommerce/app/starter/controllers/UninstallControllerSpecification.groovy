package com.e2x.bigcommerce.app.starter.controllers

import com.e2x.bigcommerce.app.starter.AuthTokenRepository
import com.e2x.bigcommerce.app.starter.SignedPayloadReader
import com.e2x.bigcommerce.app.starter.model.SignedPayload
import org.springframework.http.HttpStatus
import spock.lang.Specification

class UninstallControllerSpecification extends Specification {

    UninstallController testObj

    SignedPayloadReader signedPayloadReader = Mock()
    AuthTokenRepository authTokenRepository = Mock()

    void setup() {
        testObj = new UninstallController(signedPayloadReader, authTokenRepository)
    }

    void "it should remove all from the repository associated with the store hash"() {
        given:
        def storeHash = "a-store-hash"
        def payload = new SignedPayload(storeHash: storeHash)

        and:
        signedPayloadReader.read("a-payload") >> payload

        when:
        def result = testObj.uninstall("a-payload")

        then:
        result.statusCode == HttpStatus.OK

        and:
        1 * authTokenRepository.deleteAllFor(storeHash)
    }
}
