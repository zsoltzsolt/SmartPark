package com.example.smartpark.ui.main.fragments

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.smartpark.data.repository.ProfileRepository
import com.example.smartpark.data.repository.RegisterRepository
import com.example.smartpark.model.ProfileResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel (application: Application) : AndroidViewModel(application) {
    private val profileRepository = ProfileRepository.create(this.getApplication())
    val profileResponse = MutableLiveData<ProfileResponse>()
    val profileError = MutableLiveData<String>()
    fun me() {
        viewModelScope.launch {
            try {
                val call =
                    profileRepository.me()
                call.enqueue(object : Callback<ProfileResponse> {
                    override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                        if (response.isSuccessful) {
                            profileResponse.value = response.body()
                        } else {
                            Log.d("Error2", response.raw().toString())
                            profileError.value = "Username or password is incorrect"
                        }
                    }

                    override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                        t.localizedMessage?.let { Log.d("Error", it) }
                        profileError.value = "An error occurred: ${t.localizedMessage}"
                    }
                })
            } catch (e: Exception) {
                e.localizedMessage?.let { Log.d("Error", it) }
                profileError.value = "An error occurred: ${e.localizedMessage}"
            }
        }
    }
}