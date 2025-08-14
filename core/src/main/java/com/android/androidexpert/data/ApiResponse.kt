package com.android.androidexpert.data

sealed class ApiResponse<out R> private constructor() {
    data class Success<out T>(val data: T) : ApiResponse<T>()
    data class Error(val data: String) : ApiResponse<Nothing>()
    data object Loading : ApiResponse<Nothing>()
}