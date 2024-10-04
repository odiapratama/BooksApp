package com.problemsolver.booksapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.problemsolver.booksapp.data.model.ApiResponse
import com.problemsolver.booksapp.data.model.BookDetail
import com.problemsolver.booksapp.data.model.WantToRead
import com.problemsolver.booksapp.data.repository.BooksRepository
import com.problemsolver.booksapp.utils.safeApiCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BooksViewModel @Inject constructor(
    private val repository: BooksRepository
) : ViewModel() {

    private val _wantToRead = MutableLiveData<ApiResponse<WantToRead>>()
    val wantToRead: LiveData<ApiResponse<WantToRead>>
        get() = _wantToRead

    private val _bookDetail = MutableLiveData<ApiResponse<BookDetail>>()
    val bookDetail: LiveData<ApiResponse<BookDetail>>
        get() = _bookDetail

    fun getWantToRead() {
        viewModelScope.launch {
            _wantToRead.value = ApiResponse.loading()
            safeApiCall(
                onError = { _wantToRead.value = ApiResponse.error(it.message.toString()) }
            ) {
                repository.getWantToRead().let {
                    _wantToRead.value = ApiResponse.success(it)
                }
            }
        }
    }

    fun getBookDetail(key: String) {
        viewModelScope.launch {
            _bookDetail.value = ApiResponse.loading()
            safeApiCall(
                onError = { _bookDetail.value = ApiResponse.error(it.message.toString()) }
            ) {
                repository.getBookDetail(key).let {
                    _bookDetail.value = ApiResponse.success(it)
                }
            }
        }
    }
}
