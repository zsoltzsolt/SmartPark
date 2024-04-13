package com.example.smartpark.model

data class LoginResponseData(
    val access_token: String,
    val expires_in: Int,
    val id_token: String,
    val refresh_expires_in: Int,
    val refresh_token: String,
    val scope: String,
    val session_state: String,
    val token_type: String
)