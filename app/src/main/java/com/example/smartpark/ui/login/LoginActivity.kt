package com.example.smartpark.ui.login

import PreferencesManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.smartpark.ui.main.MainActivity
import com.example.smartpark.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()
    private val preferencesManager: PreferencesManager by lazy {
        PreferencesManager.getInstance(applicationContext)
    }
    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel.secure()

        loginViewModel.secureResponse.observe(this) { response ->
            Log.d("Response", response);
            if (loginViewModel.secureResponse.value?.toInt() == 200) {
                navigateToMainActivity()
            }
        }






        binding.btnLogin.setOnClickListener {
            val username = binding.tieEmail.text.toString()
            val password = binding.tiePassword.text.toString()
            loginViewModel.login(username, password)
        }

        loginViewModel.loginResponse.observe(this) { response ->
            saveAccessToken(response.access_token)
            navigateToMainActivity()
        }

        loginViewModel.loginError.observe(this) { error ->
            showToast(error)
        }
    }

    private fun saveAccessToken(token: String) {
        preferencesManager.saveAccessToken(token)
    }

    private fun navigateToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
