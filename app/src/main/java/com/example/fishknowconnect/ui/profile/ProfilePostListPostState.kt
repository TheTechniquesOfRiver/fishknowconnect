package com.example.fishknowconnect.ui.profile

import com.example.fishknowconnect.ui.GetPostTypeResponse

sealed class ProfilePostListPostState {
    object None: ProfilePostListPostState()
    object Loading : ProfilePostListPostState()
    data class Success(val response: List<GetPostTypeResponse>?) : ProfilePostListPostState()
    data class Error(val response: List<GetPostTypeResponse>?) : ProfilePostListPostState()
    data class Failure(val response: String) : ProfilePostListPostState()

}