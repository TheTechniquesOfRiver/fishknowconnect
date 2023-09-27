package com.example.fishknowconnect.ui.fish

sealed class FishState {
    object None: FishState()
    object Loading : FishState()
    data class Success(val response: List<GetAllPostResponse>?) : FishState()
    data class Error(val response: List<GetAllPostResponse>?) : FishState()
    data class Failure(val string: String) : FishState()

}