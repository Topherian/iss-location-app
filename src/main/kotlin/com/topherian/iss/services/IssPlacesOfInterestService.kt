package com.topherian.iss.services

import com.topherian.iss.external.feign.IssLocationClient
import com.topherian.iss.external.feign.WikiGeoSearchClient
import com.topherian.iss.external.models.iss.IssPosition
import com.topherian.iss.external.models.iss.Location
import com.topherian.iss.models.IssLocation
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

    fun findPlacesOfInterestOverride(latitude: String, longitude: String): Results {
        val issPositionOverride = IssPosition(latitude, longitude)
        val location = Location (issPositionOverride, "override", 0)
        return findPlacesOfInterestByLocation(location)
    }

    fun findPlacesOfInterest() : Results {
        val location = issLocationClient.getIssLocation()
        return findPlacesOfInterestByLocation(location)
    }
    private fun findPlacesOfInterestByLocation(location: Location): Results {

        logger.debug("Current specified ISS location: {}", location)
        val coordinates = "${location.iss_position.latitude}|${location.iss_position.longitude}"
        val results = wikiGeoSearchClient.searchPageByCoordinates(gscoord = coordinates)
        logger.debug("Result count given location: {}", results.query.geosearch.size)
        val resultsList = results.query.geosearch.map {
            val resolveCountry = it.country
            logger.debug("Resolved ISO Country code: {}", resolveCountry)
            val countryName = countryIsoResolverService.resolveCountryIso(it.country)
            Result(
                country = countryName,
                latitude = it.lat,
                longitude = it.lon,
                title = it.title
            )
        }.toList()
        val status = String.format("success - found ${resultsList.size} places of interest")
        logger.info(status)
        return resultsMapper(location, resultsList, status)
    }

    private fun resultsMapper(issCurrentLocation: Location, results: List<Result>, status: String): Results {
        return Results(
            issCurrentLocation = IssLocation
                (
                issCurrentLocation.iss_position.latitude,
                issCurrentLocation.iss_position.longitude,
                issCurrentLocation.timestamp
            ),
            results = results, status = status
        )
    }

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val logger = getLogger(javaClass.enclosingClass)
    }
}

