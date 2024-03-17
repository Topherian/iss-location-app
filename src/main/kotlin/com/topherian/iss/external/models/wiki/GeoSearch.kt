package com.topherian.iss.external.models.wiki

import com.fasterxml.jackson.annotation.JsonCreator

data class GeoSearch @JsonCreator constructor(
    val dist: Double,
    val lat: Double,
    val lon: Double,
    val ns: Int,
    val pageid: Int,
    val primary: Boolean,
    val title: String,
    val country: String?
)