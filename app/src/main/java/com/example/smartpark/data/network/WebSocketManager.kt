package com.example.smartpark.data.network

import android.util.Log
import android.widget.Toast
import okhttp3.*
import java.util.concurrent.TimeUnit

class WebSocketManager {

    private lateinit var client: OkHttpClient
    private lateinit var webSocket: WebSocket

    fun connectToWebSocket(url: String) {
        val request = Request.Builder()
            .url(url)
            .build()

        client = OkHttpClient.Builder()
            .readTimeout(0, TimeUnit.MILLISECONDS)
            .build()

        Log.d("START99", "Here")

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                // Connection opened
                Log.d("START100", "WebSocket connection opened")
                println("WebSocket connection opened")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                // Handle incoming messages
                Log.d("START101", "Received message: $text")
                println("Received message: $text")
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                // Connection closed
                Log.d("START102", "WebSocket connection closed: $reason")
                println("WebSocket connection closed: $reason")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                // Connection failure
                println("WebSocket connection failed: ${t.message}")
                Log.d("START103", "WebSocket connection failed: ${t.message}")
            }
        })
    }

    fun sendMessage(message: String) {
        webSocket.send(message)
    }

    fun closeWebSocket() {
        webSocket.close(1000, "Closing connection")
    }
}
