package com.problemsolver.event.di

import com.problemsolver.event.data.repository.EventRepository
import com.problemsolver.event.domain.EventUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    @Singleton
    fun provideEventUseCase(repository: EventRepository): EventUseCase {
        return EventUseCase(repository)
    }
}