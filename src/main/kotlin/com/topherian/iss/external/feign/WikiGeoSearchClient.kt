package com.topherian.iss.external.feign

import com.topherian.iss.configuration.ClientConfig
import com.topherian.iss.external.models.wiki.WikiPageGeoSearchResults
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(
    value = "wikiGeoSearchFeignClient",
    url = "\${iss.wiki-action.endpoint}",
    configuration = [ClientConfig::class]
)
interface WikiGeoSearchClient {

    @GetMapping(path = ["/w/api.php"])
    fun searchPageByCoordinates(
        @RequestParam(value = "action") action: String = "query",
        @RequestParam(value = "format") format: String = "json",
        @RequestParam(value = "list") list: String = "geosearch",
        @RequestParam(value = "gscoord") gscoord: String,
        @RequestParam(value = "gsprop") gsprop: String = "country",
        @RequestParam(value = "gsradius") gsradius: String = "10000",
        @RequestParam(value = "gslimit") gslimit: String = "10"
    ): WikiPageGeoSearchResults
}