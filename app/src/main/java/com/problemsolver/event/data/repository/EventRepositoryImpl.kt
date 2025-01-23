package com.problemsolver.event.data.repository

import com.problemsolver.event.data.source.EventApi
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    val eventApi: EventApi
): EventRepository {

    override suspend fun getEvents(active: Int, search: String) = eventApi.getEvents(active, search)

    override suspend fun getEventDetail(id: Int) = eventApi.getEventDetail(id)
}
