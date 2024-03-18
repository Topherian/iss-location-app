package com.topherian.iss.services

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.topherian.iss.external.feign.IssLocationClient
import com.topherian.iss.external.feign.WikiGeoSearchClient
import com.topherian.iss.external.models.iss.Location
import com.topherian.iss.external.models.wiki.WikiPageGeoSearchResults
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Given a IssPlacesOfInterestServiceTest")
class IssPlacesOfInterestServiceTest {

    private val mapper = jacksonObjectMapper()

    @Test
    fun `when the service request is made, and the wiki page results are empty, then an empty results list are returned`() {

        val issLocationClientMock = mockk<IssLocationClient>()
        val locationMock = mockk<Location>()
        every { locationMock.iss_position.latitude } returns "lat1"
        every { locationMock.iss_position.longitude } returns "long1"
        every { locationMock.timestamp } returns 123
        every { issLocationClientMock.getIssLocation() } returns locationMock


        val wikiResults = mockk<WikiPageGeoSearchResults>()
        every { wikiResults.query.geosearch } returns emptyList()

        val wikiGeoSearchClientMock = mockk<WikiGeoSearchClient>()
        every { wikiGeoSearchClientMock.searchPageByCoordinates(gscoord = any()) } returns wikiResults

        val countryIsoResolverServiceMock = mockk<CountryIsoResolverService>()

        val issPlacesOfInterestService = IssPlacesOfInterestService(
            issLocationClient = issLocationClientMock,
            wikiGeoSearchClient = wikiGeoSearchClientMock,
            countryIsoResolverService = countryIsoResolverServiceMock
        )

        val actualResults = issPlacesOfInterestService.findPlacesOfInterest()

        verify(exactly = 1) { issLocationClientMock.getIssLocation() }
        verify(atLeast = 1) { wikiResults.query.geosearch }

        actualResults.results shouldBe emptyList()
        actualResults.issCurrentLocation.latitude shouldBeEqual "lat1"
        actualResults.issCurrentLocation.longitude shouldBeEqual "long1"
        actualResults.issCurrentLocation.asOfTimeStamp shouldBeEqual 123
        actualResults.status shouldContain "success - found 0 places of interest"
    }

    @Test
    fun `when the service request is made, and the wiki page return results, then a non-empty results list are returned`() {

        val issLocationClientMock = mockk<IssLocationClient>()
        val locationMock = mockk<Location>()
        every { locationMock.iss_position.latitude } returns "lat1"
        every { locationMock.iss_position.longitude } returns "long1"
        every { locationMock.timestamp } returns 123
        every { issLocationClientMock.getIssLocation() } returns locationMock

        val wikiResults = mapper.readValue(getGeoSearchResults(), WikiPageGeoSearchResults::class.java)

        val wikiGeoSearchClientMock = mockk<WikiGeoSearchClient>()
        every { wikiGeoSearchClientMock.searchPageByCoordinates(gscoord = any()) } returns wikiResults

        val countryIsoResolverServiceMock = mockk<CountryIsoResolverService>()
        every { countryIsoResolverServiceMock.resolveCountryIso(any()) } returns "Philippines"

        val issPlacesOfInterestService = IssPlacesOfInterestService(
            issLocationClient = issLocationClientMock,
            wikiGeoSearchClient = wikiGeoSearchClientMock,
            countryIsoResolverService = countryIsoResolverServiceMock
        )

        val actualResults = issPlacesOfInterestService.findPlacesOfInterest()

        verify(exactly = 1) { issLocationClientMock.getIssLocation() }
        verify(exactly = 1) { wikiGeoSearchClientMock.searchPageByCoordinates(gscoord = any()) }
        verify(atLeast = 1) { countryIsoResolverServiceMock.resolveCountryIso(any()) }

        actualResults.results shouldNotBe null
        actualResults.results.size shouldBeEqual 10
        actualResults.results.find { it.country.isNotEmpty() }?.country shouldBe "Philippines"

        val expectedResult = wikiResults.query.geosearch.first { it.title == "Quiapo Church" }

        //validate mapped correctly
        actualResults.results.filter { it.title == expectedResult.title }.size shouldBe 1
        actualResults.results.filter { it.latitude == expectedResult.lat }.size shouldBe 1
        actualResults.results.filter { it.longitude == expectedResult.lon }.size shouldBe 1
        actualResults.status shouldContain "found 10 places of interest"
        actualResults.issCurrentLocation.latitude shouldBeEqual "lat1"
        actualResults.issCurrentLocation.longitude shouldBeEqual "long1"
        actualResults.issCurrentLocation.asOfTimeStamp shouldBeEqual 123

    }

    fun getGeoSearchResults() = """
        {
          "batchcomplete": false,
          "query": {
            "geosearch": [
              {
                "dist": 0,
                "lat": 14.599916,
                "lon": 120.984337,
                "ns": 0,
                "pageid": 37747879,
                "primary": false,
                "title": "Guzman College of Science and Technology"
              },
              {
                "dist": 138.4,
                "lat": 14.59888888888889,
                "lon": 120.98361111111112,
                "ns": 0,
                "pageid": 1268840,
                "primary": false,
                "title": "Black Nazarene",
                "country": "PH"
              },
              {
                "dist": 139.5,
                "lat": 14.598782,
                "lon": 120.983783,
                "ns": 0,
                "pageid": 11498694,
                "primary": false,
                "title": "Quiapo Church",
                "country": "PH"
              },
              {
                "dist": 144.2,
                "lat": 14.6,
                "lon": 120.983,
                "ns": 0,
                "pageid": 1268836,
                "primary": false,
                "title": "Quiapo, Manila",
                "country": "PH"
              },
              {
                "dist": 169.6,
                "lat": 14.600291,
                "lon": 120.985865,
                "ns": 0,
                "pageid": 44702694,
                "primary": false,
                "title": "Ocampo Pagoda Mansion"
              },
              {
                "dist": 173.6,
                "lat": 14.598404,
                "lon": 120.984739,
                "ns": 0,
                "pageid": 46356877,
                "primary": false,
                "title": "Bahay Nakpil-Bautista"
              },
              {
                "dist": 176.5,
                "lat": 14.59836,
                "lon": 120.98466,
                "ns": 0,
                "pageid": 44061968,
                "primary": false,
                "title": "Boix House",
                "country": "PH"
              },
              {
                "dist": 216.9,
                "lat": 14.59815,
                "lon": 120.98348,
                "ns": 0,
                "pageid": 13940164,
                "primary": false,
                "title": "Plaza Miranda bombing",
                "country": "PH"
              },
              {
                "dist": 221.1,
                "lat": 14.598055555555556,
                "lon": 120.98361111111112,
                "ns": 0,
                "pageid": 4195451,
                "primary": false,
                "title": "Plaza Miranda"
              },
              {
                "dist": 247.9,
                "lat": 14.5981,
                "lon": 120.983,
                "ns": 0,
                "pageid": 22903642,
                "primary": false,
                "title": "St. Francis Square (Bratislava)"
              }
            ]
          }
        }
    """.trimIndent()
}