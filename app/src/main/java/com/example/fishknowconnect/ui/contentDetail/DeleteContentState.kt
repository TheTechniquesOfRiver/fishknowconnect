package com.example.fishknowconnect.ui.contentDetail

import com.example.fishknowconnect.ui.login.LoginResponse

sealed class DeleteContentState {
    object None: DeleteContentState()
    object Loading : DeleteContentState()
    data class Success(val response:LoginResponse) : DeleteContentState()
    data class Error(val response: LoginResponse) : DeleteContentState()
    data class Failure(val string: String) : DeleteContentState()

}