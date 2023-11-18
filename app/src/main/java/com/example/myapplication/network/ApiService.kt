package com.example.myapplication.network

import com.example.myapplication.models.ResponseData
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("cryptocurrency/listings/latest")
    suspend fun getLatestListings(@Query("CMC_PRO_API_KEY") apiKey: String): Response<ResponseData>
}