package com.topherian.iss.controller

import com.topherian.iss.external.feign.IssLocationClient
import com.topherian.iss.external.feign.WikiGeoSearchClient
import com.topherian.iss.external.models.iss.Location
import com.topherian.iss.external.models.wiki.WikiPageGeoSearchResults
import com.topherian.iss.services.CountryIsoResolverService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

/*
Test Harness controller for manual testing
 */
@RestController
class TestHarnessController(
    private val issLocationClient: IssLocationClient,
    private val wikiGeoSearchClient: WikiGeoSearchClient,
    private val countryIsoResolverService: CountryIsoResolverService
) {

    @GetMapping("/iss-location")
    fun getIssLocation(): Location {
        return issLocationClient.getIssLocation()
    }

    @GetMapping("/places/")
    fun getPlaces(): WikiPageGeoSearchResults {
        val latitude = "14.599916"
        val longitude = "120.984337"
        val coordinates = "${latitude}|${longitude}"
        return wikiGeoSearchClient.searchPageByCoordinates(gscoord = coordinates)

    }

    @GetMapping("/countryName/{isoCode}")
    fun getCountryDetails(@PathVariable("isoCode") isoCode: String): String {
        return countryIsoResolverService.resolveCountryIso(isoCode)
    }
}