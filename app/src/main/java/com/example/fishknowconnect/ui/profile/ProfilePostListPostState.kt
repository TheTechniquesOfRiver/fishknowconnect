package com.example.fishknowconnect.ui.profile

import com.example.fishknowconnect.ui.fish.GetAllPostResponse

sealed class ProfilePostListPostState {
    object None: ProfilePostListPostState()
    object Loading : ProfilePostListPostState()
    data class Success(val response: List<GetAllPostResponse>?) : ProfilePostListPostState()
    data class Error(val response: List<GetAllPostResponse>?) : ProfilePostListPostState()
    data class Failure(val response: String) : ProfilePostListPostState()

}