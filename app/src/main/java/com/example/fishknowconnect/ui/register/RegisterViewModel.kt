package com.example.fishknowconnect.ui.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fishknowconnect.network.FishKnowConnectApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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

    //phone update
    var phone by mutableStateOf("")
        private set

    fun updatePhone(input: String) {
        phone = input
    }

    //id update
    var id by mutableStateOf("")
        private set

    fun updateId(input: String) {
        id = input
    }


    /**
     * fetch registration data
     */
    fun performRegistration() {
        viewModelScope.launch(Dispatchers.Main) {
            mutableState.value = RegistrationState.Loading
            val response =
                FishKnowConnectApi.retrofitService.register(username, password, phone, id)
            val registerResponse = response.body()
            if (registerResponse == null) {
                mutableState.value = RegistrationState.Error("response null value")
            } else {
                if (response.isSuccessful) {
                    when (response.code()) {
                        201 -> mutableState.value = RegistrationState.Success(registerResponse.message)
                    }
                } else {
                    when (response.code()) {
                        409 -> mutableState.value = RegistrationState.Error(registerResponse.message)
                    }
                }
            }

        }
    }
}