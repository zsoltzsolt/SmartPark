package com.example.smartpark.data.repository

import PreferencesManager
import android.content.Context
import com.example.smartpark.BuildConfig
import com.example.smartpark.data.network.AuthenticationInterceptor
import com.example.smartpark.data.network.LoginApiService
import com.example.smartpark.data.network.SecureApiService
import com.example.smartpark.model.LoginResponse
import com.example.smartpark.model.SecureResponse
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class SecureRepository @Inject constructor(private val secureApiService: SecureApiService) {

    fun secure(): Call<SecureResponse> {
        return try {
            val response = secureApiService.secure()
            response
        } catch (e: Exception) {
            throw RuntimeException(e.localizedMessage)
        }
    }


    companion object {
        fun create(applicationContext: Context): SecureRepository {

            val httpClient = OkHttpClient.Builder()
                .addInterceptor(AuthenticationInterceptor(PreferencesManager.getInstance(applicationContext)))
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build()

            val secureApiService = retrofit.create(SecureApiService::class.java)
            return SecureRepository(secureApiService)
        }
    }

}