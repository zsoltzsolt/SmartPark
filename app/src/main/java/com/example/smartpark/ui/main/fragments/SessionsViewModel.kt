package com.example.smartpark.ui.main.fragments

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.smartpark.data.repository.PredictRepository
import com.example.smartpark.data.repository.ProfileRepository
import com.example.smartpark.model.ProfileResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SessionsViewModel(application: Application) : AndroidViewModel(application) {
    private val predictRepository = PredictRepository.create()
    val predictResponse = MutableLiveData<String>()
    val predictError = MutableLiveData<String>()
    fun predict(start: String, end: String) {
        viewModelScope.launch {
            try {
                val call =
                    predictRepository.predict(start, end)
                call.enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if (response.isSuccessful) {
                            predictResponse.value = response.body()
                        } else {
                            Log.d("Error2", response.raw().toString())
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        t.localizedMessage?.let { Log.d("Error", it) }
                        predictError.value = "An error occurred: ${t.localizedMessage}"
                    }
                })
            } catch (e: Exception) {
                e.localizedMessage?.let { Log.d("Error", it) }
                predictError.value = "An error occurred: ${e.localizedMessage}"
            }
        }
    }
}