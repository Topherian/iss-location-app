package com.topherian.iss.external.models.iss

import com.fasterxml.jackson.annotation.JsonCreator

data class IssPosition @JsonCreator constructor(
    val latitude: String,
    val longitude: String
)

