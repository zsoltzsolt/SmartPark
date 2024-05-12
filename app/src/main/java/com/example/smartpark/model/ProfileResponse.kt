package com.example.smartpark.model

data class ProfileResponse(
    val email: String,
    val id: Int,
    val keycloakId: String,
    val stripeCustomerId: String,
    val subscriptions: List<Subscription>,
    val username: String
)