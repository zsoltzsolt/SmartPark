package com.example.smartpark.data.repository

import com.example.smartpark.BuildConfig
import com.example.smartpark.data.network.LoginApiService
import com.example.smartpark.model.LoginResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class LoginRepository @Inject constructor(private val loginApiService: LoginApiService) {

    fun login(username: String, password: String): Call<LoginResponse> {
        return try {
            val response = loginApiService.login(username, password, "parking", "openid", "password")
            response
        } catch (e: Exception) {
            throw RuntimeException(e.localizedMessage)
        }
    }


    companion object {
        fun create(): LoginRepository {
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.keycloakBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val loginApiService = retrofit.create(LoginApiService::class.java)
            return LoginRepository(loginApiService)
        }
    }
}
