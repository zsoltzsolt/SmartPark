package com.example.smartpark.data.network

import com.example.smartpark.model.RegisterBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface RegisterApiService {
    @Headers("Content-Types: application/json")
    @POST("api/v1/user-profile/register")
    fun register(@Body body: RegisterBody): Call<Void>
}