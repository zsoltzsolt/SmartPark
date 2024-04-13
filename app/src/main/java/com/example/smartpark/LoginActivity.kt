package com.example.smartpark

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.example.smartpark.databinding.ActivityLoginBinding
import com.example.smartpark.databinding.ActivityMainBinding
import com.example.smartpark.model.LoginResponseData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
private lateinit var binding: ActivityLoginBinding
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("access_token", null)
        if (accessToken != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnLogin.setOnClickListener {
            var username = binding.tieEmail.text.toString()
            var password = binding.tiePassword.text.toString()
            getMyData(username, password, this)
        }
    }

    private fun getMyData(name: String, password: String, context: Context) {
        Log.d("DATA", BuildConfig.keycloakBaseUrl)
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.keycloakBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiInterface::class.java)

        val call = apiService.login(
            username = name,
            password = password,
            clientId = "parking",
            scope = "openid",
            grantType = "password"
        )

        call.enqueue(object : Callback<LoginResponseData> {
            override fun onResponse(call: Call<LoginResponseData>, response: Response<LoginResponseData>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("Success", responseBody!!.access_token)

                    val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("access_token", responseBody!!.access_token)
                    editor.apply()

                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    Log.d("Success1", "Shit happened")

                    Toast.makeText(context, "Username or password is incorrect", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponseData>, t: Throwable) {
                Log.d("Success2", t.message.toString())

                Toast.makeText(context, "Username or password is incorrect", Toast.LENGTH_SHORT).show()
            }
        })



    }
}