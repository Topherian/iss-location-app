package com.topherian.iss.models

import com.fasterxml.jackson.annotation.JsonCreator

data class Result  @JsonCreator constructor(
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val title: String
)