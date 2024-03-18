package com.topherian.iss.models

import com.fasterxml.jackson.annotation.JsonCreator

data class IssLocation @JsonCreator constructor(
    val latitude: String,
    val longitude: String,
    val asOfTimeStamp: Int
)