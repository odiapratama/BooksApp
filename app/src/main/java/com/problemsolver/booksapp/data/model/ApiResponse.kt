package com.problemsolver.booksapp.data.model

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}

data class ApiResponse<T>(
    val status: Status = Status.SUCCESS,
    val data: T? = null,
    val message: String? = null
) {
    companion object {
        fun <T> success(data: T): ApiResponse<T> {
            return ApiResponse(Status.SUCCESS, data, null)
        }

        fun <T> error(message: String): ApiResponse<T> {
            return ApiResponse(Status.ERROR, null, message)
        }

        fun <T> loading(): ApiResponse<T> {
            return ApiResponse(Status.LOADING, null, null)
        }
    }
}