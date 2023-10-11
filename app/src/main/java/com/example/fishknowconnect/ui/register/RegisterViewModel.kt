package com.example.fishknowconnect.ui.register

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fishknowconnect.network.FishKnowConnectApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.security.AccessController.getContext


class RegisterViewModel : ViewModel() {
    private val mutableState = MutableStateFlow<RegistrationState>(RegistrationState.None)
    val state = mutableState.asStateFlow()

    //username update
    var username by mutableStateOf("")
        private set

    fun updateUsername(input: String) {
        username = input
    }

    //password update
    var password by mutableStateOf("")
        private set

    fun updatePassword(input: String) {
        password = input
    }

    //age update
    var age by mutableStateOf("")
        private set

    fun updateAge(input: String) {
        age = input
    }

    //location update
    var location by mutableStateOf("")
        private set

    fun updateLocation(input: String) {
        location = input
    }

    /**
     * fetch registration data
     */
    fun performRegistration() {
        viewModelScope.launch(Dispatchers.Main) {
            mutableState.value = RegistrationState.Loading
            val response =
                FishKnowConnectApi.retrofitService.register(username, password, age, location)
            if (response.isSuccessful) {
                val registerResponse = response.body()
                if (registerResponse != null) {
                    when (response.code()) {
                        200 -> mutableState.value =
                            RegistrationState.Success(registerResponse.message)
                        201 -> mutableState.value =
                            RegistrationState.Error(registerResponse.message)
                    }
                }
            }
        }
    }
}