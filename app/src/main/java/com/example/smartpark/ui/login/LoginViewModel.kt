package com.example.smartpark.ui.login

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.smartpark.data.network.ApiService
import com.example.smartpark.BuildConfig
import com.example.smartpark.data.repository.LoginRepository
import com.example.smartpark.model.LoginResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val loginRepository = LoginRepository.create()
    val loginResponse = MutableLiveData<LoginResponse>()
    val loginError = MutableLiveData<String>()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                val call = loginRepository.login(username, password)
                call.enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                        if (response.isSuccessful) {
                            loginResponse.value = response.body()
                        } else {
                            loginError.value = "Username or password is incorrect"
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        t.localizedMessage?.let { Log.d("Error", it) }
                        loginError.value = "An error occurred: ${t.localizedMessage}"
                    }
                })
            } catch (e: Exception) {
                e.localizedMessage?.let { Log.d("Error", it) }
                loginError.value = "An error occurred: ${e.localizedMessage}"
            }
        }
    }
}
