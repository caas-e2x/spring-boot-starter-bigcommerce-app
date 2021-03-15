package com.e2x.bigcommerce.app.starter.integrations


import com.github.tomakehurst.wiremock.WireMockServer
import spock.lang.Shared
import spock.lang.Specification

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor

abstract class WiremockSpecification extends Specification {
    @Shared
    WireMockServer wireMockServer

    void setupSpec() {
        wireMockServer = new WireMockServer(8111)
        wireMockServer.start()

        configureFor(wireMockServer.port())
    }
}
