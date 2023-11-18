package com.example.myapplication.data

data class ApiResponse<T>(
    val loading: Boolean = false,
    val data: T? = null,
    val error: Throwable? = null
)