package com.example.smartpark.ui.main

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

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val secureRepository = SecureRepository.create(applicationContext = application)
    val secureResponse = MutableLiveData<SecureResponse>()
    val loginError = MutableLiveData<String>()

    fun secure() {
        viewModelScope.launch {
            try {
                val call = secureRepository.secure()
                call.enqueue(object : Callback<SecureResponse> {
                    override fun onResponse(call: Call<SecureResponse>, response: Response<SecureResponse>) {
                        if (response.isSuccessful) {
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