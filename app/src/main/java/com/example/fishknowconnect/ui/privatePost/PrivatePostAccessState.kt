package com.example.fishknowconnect.ui.privatePost

sealed class PrivatePostAccessState {
    object None: PrivatePostAccessState()
    object Loading : PrivatePostAccessState()
    data class Success(val response: GetPrivatePostAccessResponse ) : PrivatePostAccessState()
    data class Error(val response: List<GetPrivatePostResponse>?) : PrivatePostAccessState()
    data class Failure(val string: String) : PrivatePostAccessState()

}