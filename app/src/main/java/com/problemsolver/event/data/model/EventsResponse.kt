package com.problemsolver.event.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EventsResponse(
    @Json(name = "error")
    val error: Boolean,

    @Json(name = "message")
    val message: String,

    @Json(name = "listEvents")
    val listEvents: List<Event>
)
