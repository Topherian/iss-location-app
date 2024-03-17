package com.topherian.iss.external.feign

import com.topherian.iss.configuration.ClientConfig
import com.topherian.iss.external.models.worldbank.WorldBankResults
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam


@FeignClient(
    value = "countryIsoFeignClient",
    url = "\${iss.country-iso.endpoint}",
    configuration = [ClientConfig::class]
)
interface CountryIsoClient {

    @GetMapping(path = ["/v2/country/{countryCode}"])
    fun getCountryCode(
        @PathVariable("countryCode") countryCode: String?,
        @RequestParam(value = "format") format: String = "json"
    ): WorldBankResults
}