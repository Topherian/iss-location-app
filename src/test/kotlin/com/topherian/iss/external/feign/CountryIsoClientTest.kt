package com.topherian.iss.external.feign

import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.ints.shouldBeGreaterThan
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

    @Test
    fun `when an empty ISO code given, return a valid response`() {

        val responseBody = countryIsoClient.getCountryCode("")

        if (responseBody.size == 2) {
            val responseBodyItem = responseBody[1] as ArrayList<*>
            //returns more than 1 country if empty or null is specified
            responseBodyItem.size shouldBeGreaterThan 0
        }
        responseBody shouldNotBe null
    }

    @Test
    fun `when a null ISO code given, return a valid response`() {

        val responseBody = countryIsoClient.getCountryCode(null)

        if (responseBody.size == 2) {
            val responseBodyItem = responseBody[1] as ArrayList<*>
            //returns more than 1 country if empty or null is specified
            responseBodyItem.size shouldBeGreaterThan 0
        }
        responseBody shouldNotBe null
    }
}