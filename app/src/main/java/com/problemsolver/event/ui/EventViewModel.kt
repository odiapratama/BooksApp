package com.problemsolver.event.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.problemsolver.event.data.model.ApiResponse
import com.problemsolver.event.data.model.Event
import com.problemsolver.event.data.repository.EventRepository
import com.problemsolver.event.domain.EventUseCase
import com.problemsolver.event.utils.safeApiCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val repository: EventRepository,
    private val eventUseCase: EventUseCase
) : ViewModel() {

    private val _events = MutableLiveData<ApiResponse<List<Event>>>()
    val events: LiveData<ApiResponse<List<Event>>>
        get() = _events

    private val _eventDetail = MutableLiveData<ApiResponse<Event>>()
    val eventDetail: LiveData<ApiResponse<Event>>
        get() = _eventDetail

    suspend fun getAllEvents(search: String) = eventUseCase(search)

    fun getEvents(active: Int = 0, search: String = "") {
        viewModelScope.launch {
            _events.value = ApiResponse.loading()
            safeApiCall(
                onError = { _events.value = ApiResponse.error(it.message.toString()) }
            ) {
                repository.getEvents(active, search).let {
                    _events.value = ApiResponse.success(it.listEvents)
                }
            }
        }
    }

    fun getEventDetail(id: Int) {
        viewModelScope.launch {
            _eventDetail.value = ApiResponse.loading()
            safeApiCall(
                onError = { _eventDetail.value = ApiResponse.error(it.message.toString()) }
            ) {
                repository.getEventDetail(id).let {
                    _eventDetail.value = ApiResponse.success(it.event)
                }
            }
        }
    }
}
