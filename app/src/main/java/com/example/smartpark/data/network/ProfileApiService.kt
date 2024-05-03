package com.example.smartpark.data.network

import com.example.smartpark.model.ProfileResponse
import com.example.smartpark.model.SecureResponse
import retrofit2.Call
import retrofit2.http.GET

interface ProfileApiService {
    @GET("api/v1/user-profile/me")
    fun me(): Call<ProfileResponse>

}