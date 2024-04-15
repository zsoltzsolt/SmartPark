package com.example.smartpark.util

interface TokenProvider {
    fun getAccessToken(): String
}