package com.example.smartpark.data.network

import com.example.smartpark.model.LoginResponse
import com.example.smartpark.model.SecureResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST


interface SecureApiService {
    @GET("api/v1/test/secure")
    fun secure(): Call<SecureResponse>
}