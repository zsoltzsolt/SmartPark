package com.example.smartpark.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import com.example.smartpark.databinding.ActivityLoginBinding
import com.example.smartpark.databinding.ActivityRegisterBinding
import com.example.smartpark.ui.login.LoginActivity
import com.example.smartpark.ui.login.LoginViewModel
import com.example.smartpark.ui.main.MainActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnRegister.setOnClickListener {
            val username = binding.tieUsername.text.toString()
            val email = binding.tieEmail.text.toString()
            val password = binding.tiePassword.text.toString()
            registerViewModel.register(username, username, username, email, password)
        }

        registerViewModel.registerResponse.observe(this) { response ->
            navigateToLoginActivity()
        }

    }

    private fun navigateToLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

}