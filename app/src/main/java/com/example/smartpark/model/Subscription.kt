package com.example.smartpark.model

data class Subscription(
    val id: Int,
    val isActive: Boolean,
    val licencePlate: String,
    val stripePriceId: String,
    val stripeSubscriptionId: String,
    val userProfileId: Int,
    val validThru: String
)