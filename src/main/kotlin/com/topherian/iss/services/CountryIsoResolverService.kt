package com.topherian.iss.services

import com.topherian.iss.external.feign.CountryIsoClient
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.rmi.UnexpectedException

@Service
class CountryIsoResolverService(private val countryIsoClient: CountryIsoClient) {

    fun resolveCountryIso(isoCode: String?): String {
        val responseBody = countryIsoClient.getCountryCode(isoCode, "json")
        //response has both header and value payloads in a single JSON array without appropriate JSON object types.
        //the array has different strongly typed domain objects within it, making it difficult to deserialize.
        //example payload: http://api.worldbank.org/v2/country/ph?format=json
        if (responseBody.size == 2) {
            val responseBodyItem = responseBody[1] as ArrayList<*>
            if (responseBodyItem.size == 1) {
                val linkedHashMap = responseBodyItem[0] as LinkedHashMap<*, *>
                if (linkedHashMap.containsKey("name")) {
                    return linkedHashMap["name"].toString()
                } else {
                    logger.warn("Payload valid, but name key not found in the payload.  Returning empty string")
                    return ""
                }
            } else {
                throw UnexpectedException("Payload is not valid or unrecognized.")
            }
        } else {
            throw UnexpectedException("Payload is not valid or unrecognized")
        }
    }

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val logger = LoggerFactory.getLogger(javaClass.enclosingClass)
    }
}