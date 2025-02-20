package com.problemsolver.event.data.repository

import androidx.datastore.core.DataStore
import com.problemsolver.event.Settings
import com.problemsolver.event.data.model.Event
import com.problemsolver.event.data.source.local.EventDao
import com.problemsolver.event.data.source.remote.EventApi
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val eventApi: EventApi,
    private val eventDao: EventDao,
    private val appPrefs: DataStore<Settings>
): EventRepository {
    override suspend fun getEvents(active: Int, search: String) = eventApi.getEvents(active, search)
    override suspend fun getEventDetail(id: Int) = eventApi.getEventDetail(id)
    override suspend fun insertFavoriteEvent(event: Event) = eventDao.insertFavoriteEvents(event)
    override suspend fun getFavoriteEvents() = eventDao.getFavoriteEvents()
    override suspend fun deleteFavoriteEvent(id: Int) = eventDao.deleteFavoriteEvent(id)
    override suspend fun getFavoriteEventById(id: Int) = eventDao.getFavoriteEventById(id)
    override suspend fun getAppPreference() = appPrefs.data
}
