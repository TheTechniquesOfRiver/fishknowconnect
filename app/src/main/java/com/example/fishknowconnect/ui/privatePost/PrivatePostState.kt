package com.example.fishknowconnect.ui.privatePost

sealed class PrivatePostState {
    object None: PrivatePostState()
    object Loading : PrivatePostState()
    data class Success(val response: List<GetPrivatePostResponse>?) : PrivatePostState()
    data class Error(val response: List<GetPrivatePostResponse>?) : PrivatePostState()
    data class Failure(val string: String) : PrivatePostState()

}