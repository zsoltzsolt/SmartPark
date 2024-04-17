package com.example.smartpark.data.network

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.smartpark.model.WebSocketResponse
import com.google.gson.Gson
import okhttp3.*
import java.util.concurrent.TimeUnit


class WebSocketManager(private val context: Context) {

    private lateinit var client: OkHttpClient
    private lateinit var webSocket: WebSocket
    private var messageListener: ((WebSocketResponse) -> Unit)? = null

    fun connectToWebSocket(url: String, listener: (WebSocketResponse) -> Unit) {
        val request = Request.Builder()
            .url(url)
            .build()

        client = OkHttpClient.Builder()
            .readTimeout(0, TimeUnit.MILLISECONDS)
            .build()

        messageListener = listener

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                println("WebSocket connection opened")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                val response = parseWebSocketResponse(text)
                Log.d("WebSocketH", response.toString())
                (context as Activity).runOnUiThread {
                    messageListener?.invoke(response)
                }
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                Log.d("WebSocketH", "WebSocket connection closed: $reason")
                println("WebSocket connection closed: $reason")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                println("WebSocket connection failed: ${t.message}")
                Log.d("WebSocketH", "WebSocket connection failed: ${t.message}")
            }
        })
    }

    fun sendMessage(message: String) {
        webSocket.send(message)
    }

    fun closeWebSocket() {
        webSocket.close(1000, "Closing connection")
    }

    private fun parseWebSocketResponse(text: String): WebSocketResponse {
        val gson = Gson()
        return gson.fromJson(text, WebSocketResponse::class.java)
    }
}
