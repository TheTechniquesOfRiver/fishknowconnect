package com.example.fishknowconnect.ui.profile

sealed class ProfileState {
    object None: ProfileState()
    object Loading : ProfileState()
    data class Success(val age: String, val location: String) : ProfileState()
    data class Error(val message: String) : ProfileState()

}