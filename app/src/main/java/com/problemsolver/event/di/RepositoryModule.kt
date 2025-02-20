package com.problemsolver.event.di

import androidx.datastore.core.DataStore
import com.problemsolver.event.Settings
import com.problemsolver.event.data.repository.EventRepository
import com.problemsolver.event.data.repository.EventRepositoryImpl
import com.problemsolver.event.data.source.local.EventDao
import com.problemsolver.event.data.source.remote.EventApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideEventRepository(
        eventApi: EventApi,
        eventDao: EventDao,
        appPreferences: DataStore<Settings>
    ): EventRepository {
        return EventRepositoryImpl(eventApi, eventDao, appPreferences)
    }
}