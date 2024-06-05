package com.example.smartpark.ui.main.fragments

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.smartpark.data.repository.PredictRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SessionsViewModel(application: Application) : AndroidViewModel(application) {
    private val predictRepository = PredictRepository.create()
    val predictResponse = MutableLiveData<Map<String, Double>?>()
    val predictError = MutableLiveData<String>()

    fun predict(start: String, end: String) {
        viewModelScope.launch {
            try {
                val call = predictRepository.predict(start, end)
                call.enqueue(object : Callback<Map<String, Double>> {
                    override fun onResponse(call: Call<Map<String, Double>>, response: Response<Map<String, Double>>) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            responseBody?.let {
                                predictResponse.value = responseBody
                            }
                        } else {
                            Log.d("Error2", response.raw().toString())
                        }
                    }

                    override fun onFailure(call: Call<Map<String, Double>>, t: Throwable) {
                        t.localizedMessage?.let { Log.d("Error777", it) }
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