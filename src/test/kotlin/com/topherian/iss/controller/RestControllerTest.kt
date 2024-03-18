package com.topherian.iss.controller

import com.ninjasquad.springmockk.MockkBean
import com.topherian.iss.models.Result
import com.topherian.iss.models.Results
import com.topherian.iss.services.IssPlacesOfInterestService
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.string.shouldContain
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap


@DisplayName("Given an ISS REST controller ")
@WebMvcTest(RestController::class)
class RestControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockkBean(relaxed = true)
    private lateinit var issPlacesOfInterestService: IssPlacesOfInterestService

    @Test
    fun `when a GET request is executed then a valid response is returned with 0 results`() {

        val expectedResults = mockk<Results>()
        every { expectedResults.issCurrentLocation.latitude } returns "lat1"
        every { expectedResults.issCurrentLocation.longitude } returns "long1"
        every { expectedResults.issCurrentLocation.asOfTimeStamp } returns 0
        every { expectedResults.status } returns "test"

        every { expectedResults.results } returns emptyList()
        every { issPlacesOfInterestService.findPlacesOfInterest() } returns expectedResults
        mockMvc.perform(get("/iss-location/places-of-interest"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string("{\"issCurrentLocation\":{\"latitude\":\"lat1\",\"longitude\":\"long1\",\"asOfTimeStamp\":0},\"results\":[],\"status\":\"test\"}"))
            .andReturn()

        verify(exactly = 1) { issPlacesOfInterestService.findPlacesOfInterest() }

    }

    @Test
    fun `when a GET request is executed then a valid response is returned with 1 result`() {

        val expectedResults = mockk<Results>()
        val result1 = mockk<Result>()
        every { expectedResults.issCurrentLocation.latitude } returns "lat1"
        every { expectedResults.issCurrentLocation.longitude } returns "long1"
        every { expectedResults.issCurrentLocation.asOfTimeStamp } returns 0
        every { expectedResults.status } returns "test"
        every { result1.title } returns "Place1"
        every { result1.country } returns "Country1"
        every { result1.latitude } returns 1.0
        every { result1.longitude } returns 2.0
        every { expectedResults.results } returns emptyList()
        every { issPlacesOfInterestService.findPlacesOfInterest() } returns expectedResults
        every { expectedResults.results } returns listOf(result1)

        mockMvc.perform(get("/iss-location/places-of-interest"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string("{\"issCurrentLocation\":{\"latitude\":\"lat1\",\"longitude\":\"long1\",\"asOfTimeStamp\":0},\"results\":[{\"country\":\"Country1\",\"latitude\":1.0,\"longitude\":2.0,\"title\":\"Place1\"}],\"status\":\"test\"}"))
            .andReturn()

        verify(exactly = 1) { issPlacesOfInterestService.findPlacesOfInterest() }
    }

    @Test
    fun `when an override GET request with coordinates is executed then a valid response is returned with 1 result`() {

        val expectedResults = mockk<Results>()
        val result1 = mockk<Result>()
        every { expectedResults.issCurrentLocation.latitude } returns "lat1"
        every { expectedResults.issCurrentLocation.longitude } returns "long1"
        every { expectedResults.issCurrentLocation.asOfTimeStamp } returns 0
        every { expectedResults.status } returns "test"
        every { result1.title } returns "Place1"
        every { result1.country } returns "Country1"
        every { result1.latitude } returns 1.0
        every { result1.longitude } returns 2.0
        every { expectedResults.results } returns emptyList()
        every { issPlacesOfInterestService.findPlacesOfInterestOverride(any(), any()) } returns expectedResults
        every { expectedResults.results } returns listOf(result1)

        mockMvc.perform(get("/iss-location/places-of-interest/override/latitude/{lat}/longitude/{long}", "lat1", "long1"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string("{\"issCurrentLocation\":{\"latitude\":\"lat1\",\"longitude\":\"long1\",\"asOfTimeStamp\":0},\"results\":[{\"country\":\"Country1\",\"latitude\":1.0,\"longitude\":2.0,\"title\":\"Place1\"}],\"status\":\"test\"}"))
            .andReturn()

        verify(exactly = 1) { issPlacesOfInterestService.findPlacesOfInterestOverride(any(), any()) }
    }

    @Test
    fun `when a GET request is executed, and when the service fails, then a valid error HTTP status response is returned with 0 results`() {

        val expectedResults = mockk<Results>()
        every { expectedResults.results } returns emptyList()
        every { issPlacesOfInterestService.findPlacesOfInterest() } throws Exception("Find Places of Interest Service failed.")

        val exception = shouldThrow<Exception> {
            issPlacesOfInterestService.findPlacesOfInterest()
        }

        mockMvc.perform(get("/iss-location/places-of-interest"))
            .andExpect(status().is5xxServerError)
            .andReturn()

        verify { issPlacesOfInterestService.findPlacesOfInterest() }
        exception.message shouldContain ("Service failed.")
    }
}