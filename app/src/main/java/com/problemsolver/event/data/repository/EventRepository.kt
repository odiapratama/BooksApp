package com.problemsolver.event.data.repository

import com.problemsolver.event.Settings
import com.problemsolver.event.data.model.Event
import com.problemsolver.event.data.model.EventDetailResponse
import com.problemsolver.event.data.model.EventsResponse
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    suspend fun getEvents(active: Int = 0, search: String = ""): EventsResponse
    suspend fun getEventDetail(id: Int = 0): EventDetailResponse
    suspend fun insertFavoriteEvent(event: Event)
    suspend fun getFavoriteEvents(): Flow<List<Event>>
    suspend fun deleteFavoriteEvent(id: Int)
    suspend fun getFavoriteEventById(id: Int): Event?
    suspend fun getAppPreference(): Flow<Settings>
}