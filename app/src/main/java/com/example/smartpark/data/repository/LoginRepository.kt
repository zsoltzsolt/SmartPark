package com.example.smartpark.data.repository

import com.example.smartpark.BuildConfig
import com.example.smartpark.data.network.ApiService
import com.example.smartpark.model.LoginResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class LoginRepository @Inject constructor(private val apiService: ApiService) {

    fun login(username: String, password: String): Call<LoginResponse> {
        return try {
            val response = apiService.login(username, password, "parking", "openid", "password")
            response
        } catch (e: Exception) {
            throw RuntimeException("${e.localizedMessage}")
        }
    }


    companion object {
        fun create(): LoginRepository {
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.keycloakBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiService = retrofit.create(ApiService::class.java)
            return LoginRepository(apiService)
        }
    }
}
