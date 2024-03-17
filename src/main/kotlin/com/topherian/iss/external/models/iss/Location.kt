package com.topherian.iss.external.models.iss

import com.fasterxml.jackson.annotation.JsonCreator

data class Location @JsonCreator constructor(
    val iss_position: IssPosition,
    val message: String,
    val timestamp: Int
)

