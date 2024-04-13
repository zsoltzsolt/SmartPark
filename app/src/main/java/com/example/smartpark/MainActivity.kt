package com.example.smartpark

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebViewClient
import com.example.smartpark.databinding.ActivityMainBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.smartpark.BuildConfig


private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        getMyData();

    }

    private fun getMyData() {
        Log.d("DATA", BuildConfig.keycloakBaseUrl)
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.keycloakBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiInterface::class.java)

        val call = apiService.login(
            username = "user",
            password = "user",
            clientId = "parking",
            scope = "openid",
            grantType = "password"
        )

        call.enqueue(object : Callback<LoginResponseData> {
            override fun onResponse(call: Call<LoginResponseData>, response: Response<LoginResponseData>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("Success", responseBody!!.access_token)
                }else{
                    Log.d("Error", "Shit happened")
                }
            }

            override fun onFailure(call: Call<LoginResponseData>, t: Throwable) {
                Log.d("Error1", t.message.toString())
            }
        })



    }
}




