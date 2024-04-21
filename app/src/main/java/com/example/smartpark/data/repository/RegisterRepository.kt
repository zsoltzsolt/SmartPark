package com.example.smartpark.data.repository

import com.example.smartpark.BuildConfig
import com.example.smartpark.data.network.RegisterApiService
import com.example.smartpark.model.RegisterBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class RegisterRepository @Inject constructor(private val registerApiService: RegisterApiService) {

    fun register(
        username: String,
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Call<Void> {
        return try {
            val response = registerApiService.register(RegisterBody(username, firstName, lastName, email, password))
            response
        } catch (e: Exception) {
            throw RuntimeException(e.localizedMessage)
        }
    }

    companion object {
        fun create(): RegisterRepository {
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val registerApiService = retrofit.create(RegisterApiService::class.java)
            return RegisterRepository(registerApiService)
        }
    }
}