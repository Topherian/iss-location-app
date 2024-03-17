package com.topherian.iss.services

import com.topherian.iss.external.feign.IssLocationClient
import com.topherian.iss.external.feign.WikiGeoSearchClient
import com.topherian.iss.models.Result
import com.topherian.iss.models.Results
import org.slf4j.LoggerFactory.getLogger
import org.springframework.stereotype.Service

@Service
class IssPlacesOfInterestService(
    private val issLocationClient: IssLocationClient,
    private val wikiGeoSearchClient: WikiGeoSearchClient,
    private val countryIsoResolverService: CountryIsoResolverService
) {

    fun findPlacesOfInterest(): Results {

        val location = issLocationClient.getIssLocation()
        logger.debug("Current ISS location: {}", location)
        val coordinates = "${location.iss_position.latitude}|${location.iss_position.longitude}"
        val results = wikiGeoSearchClient.searchPageByCoordinates(gscoord = coordinates)
        logger.debug("Result count given location: {}", results.query.geosearch.size)
        if (results.query.geosearch.isNotEmpty()) {
            val resolveCountry = results.query.geosearch.find { it.country?.isNotEmpty() == true }?.country
            logger.debug("Resolved ISO Country code: {}", resolveCountry)
            //assumption: the result set will all return from the same country
            val countryName = countryIsoResolverService.resolveCountryIso(resolveCountry)
            val resultsList = results.query.geosearch.map {
                Result(
                    country = countryName,
                    latitude = it.lat,
                    longitude = it.lon,
                    title = it.title
                )
            }.toList()
            return Results(resultsList)
        }
        return Results(results = emptyList())

    }

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val logger = getLogger(javaClass.enclosingClass)
    }
}

