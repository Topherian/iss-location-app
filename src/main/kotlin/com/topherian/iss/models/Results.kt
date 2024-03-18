package com.topherian.iss.models

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.databind.annotation.JsonSerialize

data class Results  @JsonCreator constructor(
    val issCurrentLocation: IssLocation,
    val results: List<Result>,
    val status: String
)