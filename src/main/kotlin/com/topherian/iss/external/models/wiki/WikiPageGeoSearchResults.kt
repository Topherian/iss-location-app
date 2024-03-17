package com.topherian.iss.external.models.wiki

import com.fasterxml.jackson.annotation.JsonCreator

data class WikiPageGeoSearchResults @JsonCreator constructor(
    val batchcomplete: Boolean,
    val query: Query
)