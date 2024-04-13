package com.example.smartpark

import retrofit2.http.Field

data class LoginFormData(
    @Field("username") val username: String,
    @Field("password") val password: String,
    @Field("client_id") val clientId: String,
    @Field("scope") val scope: String,
    @Field("grant_type") val grantType: String
)

