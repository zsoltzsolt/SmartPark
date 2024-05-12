package com.example.smartpark.data.repository

import com.example.smartpark.BuildConfig
import com.example.smartpark.data.network.PredictApiService
import com.example.smartpark.model.PredictBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class PredictRepository @Inject constructor(private val predictApiService: PredictApiService) {

    fun predict(
        start: String,
        end: String
    ): Call<String> {
        return try {
            val response = predictApiService.predict(PredictBody(start, end))
            response
        } catch (e: Exception) {
            throw RuntimeException(e.localizedMessage)
        }
    }

    companion object {
        fun create(): PredictRepository {
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val predictApiService = retrofit.create(PredictApiService::class.java)
            return PredictRepository(predictApiService)
        }
    }
}