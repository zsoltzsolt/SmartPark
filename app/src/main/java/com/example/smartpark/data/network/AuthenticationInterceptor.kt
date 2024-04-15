package com.example.smartpark.data.network

import PreferencesManager
import com.example.smartpark.ui.login.LoginActivity
import com.example.smartpark.util.TokenProvider
import okhttp3.Interceptor
import okhttp3.Response

class AuthenticationInterceptor(private val tokenProvider: TokenProvider) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val original = chain.request()

        if (original.header("Authorization") != null) {
            return chain.proceed(original)
        }

        val token = tokenProvider.getAccessToken()
        val requestBuilder = original.newBuilder()

        token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}
