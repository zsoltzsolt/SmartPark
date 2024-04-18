package com.example.smartpark.model

data class WebSocketResponse(
    val createdAt: Double,
    val id: Int,
    val parkingSpot1: Boolean,
    val parkingSpot2: Boolean,
    val parkingSpot3: Boolean,
    val parkingSpot4: Boolean,
    val parkingSpot5: Boolean,
    val parkingSpot6: Boolean,
    val parkingSpot7: Boolean,
    val parkingSpot8: Boolean
)