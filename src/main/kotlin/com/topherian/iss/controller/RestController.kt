package com.topherian.iss.controller

import com.topherian.iss.models.Results
import com.topherian.iss.services.IssPlacesOfInterestService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
class RestController(private val issPlacesOfInterestService: IssPlacesOfInterestService) {

    @GetMapping("/iss-location/places-of-interest")
    fun getIssLocationPlacesOfInterest(): Results {
        try {
            return issPlacesOfInterestService.findPlacesOfInterest()
        } catch (ex: Exception) {
            logger.error("An exception has occurred", ex)
            throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "An exception has occurred with the service", ex
            )
        }
    }

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val logger = LoggerFactory.getLogger(javaClass.enclosingClass)
    }
}