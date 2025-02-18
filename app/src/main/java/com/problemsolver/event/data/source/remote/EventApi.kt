package com.problemsolver.event.data.source.remote

import com.problemsolver.event.data.model.EventDetailResponse
import com.problemsolver.event.data.model.EventsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EventApi {

    @GET("events")
    suspend fun getEvents(
        @Query("active") active: Int,
        @Query("q") search: String
    ): EventsResponse

    @GET("events/{id}")
    suspend fun getEventDetail(
        @Path("id") id: Int
    ): EventDetailResponse
}