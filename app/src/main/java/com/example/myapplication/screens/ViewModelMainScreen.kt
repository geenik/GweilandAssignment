package com.example.myapplication.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.ApiResponse
import com.example.myapplication.models.ResponseData
import com.example.myapplication.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ViewModelMainScreen : ViewModel() {
    private val _apiResponse = MutableStateFlow(ApiResponse<ResponseData>())
    val apiResponse: StateFlow<ApiResponse<ResponseData>> = _apiResponse

    private val api=RetrofitInstance.apiService

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch(Dispatchers.IO) {
            _apiResponse.value = ApiResponse(loading = true)
            val apiKey =  RetrofitInstance.API_KEY
             try {
                 val response=api.getLatestListings(apiKey)
                 if(response.isSuccessful){
                     _apiResponse.value= ApiResponse(data=response.body())
                 }else{
                     Log.d("else", response.message())
                 }
             }catch (e:Exception){
                 Log.d("catch", e.message.toString())
                 _apiResponse.value=ApiResponse(error = e)
             }
        }
    }
}