package com.example.smartpark.data.network

import com.example.smartpark.model.LoginResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("realms/parkingSI/protocol/openid-connect/token")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("client_id") clientId: String,
        @Field("scope") scope: String,
        @Field("grant_type") grantType: String
    ): Call<LoginResponse>
}