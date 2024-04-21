package com.example.smartpark.model

import retrofit2.http.Body

data class RegisterBody(
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String
)