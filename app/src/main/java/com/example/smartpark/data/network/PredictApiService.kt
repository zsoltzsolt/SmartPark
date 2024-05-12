package com.example.smartpark.data.network

import com.example.smartpark.model.PredictBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface PredictApiService {
    @POST("api/v1/predict/interval")
    fun predict(@Body body: PredictBody): Call<Map<String, Double>>

}