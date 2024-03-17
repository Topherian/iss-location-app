package com.topherian.iss.external.feign

import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT

@DisplayName("Feign client connectivity test")
@SpringBootTest(webEnvironment = DEFINED_PORT)
class CountryIsoClientTest(@Autowired val countryIsoClient: CountryIsoClient) {

    @Test
    fun `when valid ISO code given, return a valid response`() {

        val response = countryIsoClient.getCountryCode("PH")

        response shouldNotBe null
    }
}