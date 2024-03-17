package com.topherian.iss.external.feign

import io.kotest.matchers.doubles.shouldBeGreaterThan
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@DisplayName("Feign client connectivity test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class WikiGeoSearchClientTest(@Autowired val wikiGeoSearchClient: WikiGeoSearchClient) {

    @Test
    fun `when a valid gs coordinate is requested, return a valid response`() {

        val latitude = "14.599916"
        val longitude = "120.984337"
        val coordinates = "${latitude}|${longitude}"
        val response = wikiGeoSearchClient.searchPageByCoordinates(gscoord = coordinates)

        response.query.geosearch.size shouldBeGreaterThan 0
        response.query.geosearch.first().lat shouldBeGreaterThan 0.0
        response.query.geosearch.first().lon shouldBeGreaterThan 0.0
        response.query.geosearch.first().title shouldNotBe null
    }
}