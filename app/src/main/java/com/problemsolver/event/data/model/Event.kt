package com.problemsolver.event.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Event(
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "summary")
    val summary: String,
    @Json(name = "description")
    val description: String,
    @Json(name = "imageLogo")
    val imageLogo: String,
    @Json(name = "mediaCover")
    val mediaCover: String,
    @Json(name = "category")
    val category: String,
    @Json(name = "ownerName")
    val ownerName: String,
    @Json(name = "cityName")
    val cityName: String,
    @Json(name = "quota")
    val quota: Int,
    @Json(name = "registrants")
    val registrants: Int,
    @Json(name = "beginTime")
    val beginTime: String,
    @Json(name = "endTime")
    val endTime: String,
    @Json(name = "link")
    val link: String
)