package com.topherian.iss.external.models.wiki

import com.fasterxml.jackson.annotation.JsonCreator

data class Query @JsonCreator constructor(
    val geosearch: List<GeoSearch>
)