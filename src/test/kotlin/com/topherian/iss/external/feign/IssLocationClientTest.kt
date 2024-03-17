package com.topherian.iss.external.feign

import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT

@DisplayName("Feign client connectivity test")
@SpringBootTest(webEnvironment = DEFINED_PORT)
class IssLocationClientTest(@Autowired val issLocationClient: IssLocationClient) {

    @Test
    fun `when the ISS location is requested, return a valid response`() {

        val response = issLocationClient.getIssLocation()

        response.iss_position.latitude shouldNotBe null
        response.iss_position.longitude shouldNotBe null
        response.message shouldNotBe null
        response.timestamp shouldBeGreaterThan 0
    }
}