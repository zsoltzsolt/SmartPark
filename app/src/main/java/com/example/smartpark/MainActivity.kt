package com.example.smartpark

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import com.example.smartpark.databinding.ActivityMainBinding

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        webViewSetup()
    }

    private fun webViewSetup() {
        binding.wbWebView.webViewClient = WebViewClient()
        binding.wbWebView.apply {
            loadUrl("https://www.google.com/")
            settings.javaScriptEnabled = true
            settings.safeBrowsingEnabled = true
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if(binding.wbWebView.canGoBack())
            binding.wbWebView.goBack()
        else
            super.onBackPressed()
    }
}