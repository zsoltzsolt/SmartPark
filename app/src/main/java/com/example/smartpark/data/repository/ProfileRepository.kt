package com.example.smartpark.data.repository

import android.content.Context
import com.example.smartpark.BuildConfig
import com.example.smartpark.data.network.AuthenticationInterceptor
import com.example.smartpark.data.network.ProfileApiService
import com.example.smartpark.data.network.SecureApiService
import com.example.smartpark.model.ProfileResponse
import com.example.smartpark.model.SecureResponse
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class ProfileRepository @Inject constructor(private val profileApiService: ProfileApiService) {

    fun me(): Call<ProfileResponse> {
        return try {
            val response = profileApiService.me()
            response
        } catch (e: Exception) {
            throw RuntimeException(e.localizedMessage)
        }
    }


    companion object {
        fun create(applicationContext: Context): ProfileRepository {

            val httpClient = OkHttpClient.Builder()
                .addInterceptor(AuthenticationInterceptor(PreferencesManager.getInstance(applicationContext)))
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build()

            val profileApiService = retrofit.create(ProfileApiService::class.java)
            return ProfileRepository(profileApiService)
        }
    }

}