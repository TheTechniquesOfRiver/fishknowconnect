package com.example.fishknowconnect.ui.login

sealed class LoginState {
    object None: LoginState()
    object Loading : LoginState()
    data class Success(val message: String) : LoginState()
    data class Error(val message: String) : LoginState()

}