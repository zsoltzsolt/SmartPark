package com.example.smartpark.ui.login

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.smartpark.data.repository.LoginRepository
import com.example.smartpark.data.repository.SecureRepository
import com.example.smartpark.model.LoginResponse
import com.example.smartpark.model.SecureResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val loginRepository = LoginRepository.create()
    private val secureRepository = SecureRepository.create(applicationContext = application)
    val secureResponse = MutableLiveData<String>()
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

    fun secure() {
        viewModelScope.launch {
            try {
                val call = secureRepository.secure()
                call.enqueue(object : Callback<SecureResponse> {
                    override fun onResponse(call: Call<SecureResponse>, response: Response<SecureResponse>) {
                        if (response.isSuccessful) {
                            secureResponse.value = response.code().toString()
                            Log.d("Success", response.code().toString())
                        } else {
                            Log.d("Error10", response.code().toString())
                            loginError.value = "Error"
                        }
                    }

                    override fun onFailure(call: Call<SecureResponse>, t: Throwable) {
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
