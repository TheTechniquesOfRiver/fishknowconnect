package com.example.fishknowconnect.ui.register

sealed class RegistrationState {
    object None: RegistrationState()
    object Loading : RegistrationState()
    data class Success(val response: String) : RegistrationState()
    data class Error(val response: String) : RegistrationState()

}