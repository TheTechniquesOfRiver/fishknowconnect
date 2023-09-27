package com.example.fishknowconnect.ui.newPost

sealed class NewPostState {
    object None: NewPostState()
    object Loading : NewPostState()
    data class Success(val message: String) : NewPostState()
    data class Error(val response: String) : NewPostState()

}