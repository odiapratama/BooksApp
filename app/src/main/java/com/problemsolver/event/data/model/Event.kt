package com.problemsolver.event.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(tableName = "event")
@JsonClass(generateAdapter = true)
data class Event(
    @Json(name = "id")
    @PrimaryKey val id: Int,

    @Json(name = "name")
    @ColumnInfo(name = "name")
    val name: String,

    @Json(name = "summary")
    @ColumnInfo(name = "summary")
    val summary: String,

    @Json(name = "description")
    @ColumnInfo(name = "description")
    val description: String,

    @Json(name = "imageLogo")
    @ColumnInfo(name = "imageLogo")
    val imageLogo: String,

    @Json(name = "mediaCover")
    @ColumnInfo(name = "mediaCover")
    val mediaCover: String,

    @Json(name = "category")
    @ColumnInfo(name = "category")
    val category: String,

    @Json(name = "ownerName")
    @ColumnInfo(name = "ownerName")
    val ownerName: String,

    @Json(name = "cityName")
    @ColumnInfo(name = "cityName")
    val cityName: String,

    @Json(name = "quota")
    @ColumnInfo(name = "quota")
    val quota: Int,

    @Json(name = "registrants")
    @ColumnInfo(name = "registrants")
    val registrants: Int,

    @Json(name = "beginTime")
    @ColumnInfo(name = "beginTime")
    val beginTime: String,

    @Json(name = "endTime")
    @ColumnInfo(name = "endTime")
    val endTime: String,

    @Json(name = "link")
    @ColumnInfo(name = "link")
    val link: String
)