package com.problemsolver.event.domain

import com.problemsolver.event.data.model.AllEvent
import com.problemsolver.event.data.model.ApiResponse
import com.problemsolver.event.data.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class EventUseCase @Inject constructor(
    private val repository: EventRepository
) {
    operator fun invoke(search: String): Flow<ApiResponse<AllEvent>> {
        return flow {
            emit(ApiResponse.loading())
            try {
                val upcomingData = repository.getEvents(1, search)
                val finishedData = repository.getEvents(0, search)
                val combinedData = AllEvent(upcomingData.listEvents, finishedData.listEvents)
                emit(ApiResponse.success(combinedData))
            } catch (e: Exception) {
                emit(ApiResponse.error(e.message.toString()))
            }
        }
    }
}