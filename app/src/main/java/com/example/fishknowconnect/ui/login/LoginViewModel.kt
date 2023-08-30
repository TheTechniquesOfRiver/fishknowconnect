package com.example.fishknowconnect.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fishknowconnect.network.FishKnowConnectApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.await
import retrofit2.awaitResponse

class LoginViewModel() : ViewModel() {
    private var _onLoginSuccess = MutableLiveData<Boolean>()
    val onLoginSuccess: LiveData<Boolean>
        get() = _onLoginSuccess

    /**
     * fetch login data
     */
    fun getLogin(loginData: LoginData) {
        viewModelScope.launch(Dispatchers.Main) {
            val response =
                FishKnowConnectApi.retrofitService.login(loginData.username, loginData.password)
            if (response.isSuccessful) {
                _onLoginSuccess.value = true
            }
        }
    }
}