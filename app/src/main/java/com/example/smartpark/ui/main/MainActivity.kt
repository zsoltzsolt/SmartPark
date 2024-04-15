package com.example.smartpark.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.smartpark.databinding.ActivityLoginBinding
import com.example.smartpark.databinding.ActivityMainBinding
import com.example.smartpark.ui.login.LoginActivity
import com.example.smartpark.ui.login.LoginViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        binding.btnLogout.setOnClickListener{
            /*val sharedPreferences = this.getSharedPreferences("MyPrefs", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.remove("access_token")
            editor.apply()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()*/
            mainViewModel.secure()
        }

        mainViewModel.secureResponse.observe(this) { response ->
            Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show()
        }

        mainViewModel.loginError.observe(this) { error ->
            Toast.makeText(this, "Error1", Toast.LENGTH_SHORT).show()
        }

    }

}