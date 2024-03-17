package com.topherian.iss.external.feign

import com.topherian.iss.configuration.ClientConfig
import com.topherian.iss.external.models.iss.Location
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(value = "issLocationFeignClient", url = "\${iss.location.endpoint}", configuration = [ClientConfig::class])
interface IssLocationClient {

    @GetMapping(path = ["/iss-now.json"])
    fun getIssLocation(): Location
}