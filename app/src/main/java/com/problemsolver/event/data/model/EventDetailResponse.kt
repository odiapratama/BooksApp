package com.problemsolver.event.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EventDetailResponse(
    @Json(name = "error")
    val error: Boolean,

    @Json(name = "message")
    val message: String,

    @Json(name = "event")
    val event: Event
)
