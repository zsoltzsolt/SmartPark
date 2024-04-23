package com.example.smartpark.ui.register

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.smartpark.data.repository.RegisterRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel (application: Application) : AndroidViewModel(application) {
    private val registerRepository = RegisterRepository.create()
    val registerResponse = MutableLiveData<Void>()
    val registerError = MutableLiveData<String>()
    fun register(
        username: String,
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            try {
                val call =
                    registerRepository.register(username, firstName, lastName, email, password)
                call.enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            registerResponse.value = response.body()
                        } else {
                             Log.d("Error2", response.raw().toString())
                            registerError.value = "Username or password is incorrect"
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        t.localizedMessage?.let { Log.d("Error", it) }
                        registerError.value = "An error occurred: ${t.localizedMessage}"
                    }
                })
            } catch (e: Exception) {
                e.localizedMessage?.let { Log.d("Error", it) }
                registerError.value = "An error occurred: ${e.localizedMessage}"
            }
        }
    }
}