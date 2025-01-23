package com.problemsolver.event.data.repository

import com.problemsolver.event.data.model.EventDetailResponse
import com.problemsolver.event.data.model.EventsResponse

interface EventRepository {
    suspend fun getEvents(active: Int = 0, search: String = ""): EventsResponse
    suspend fun getEventDetail(id: Int = 0): EventDetailResponse
}