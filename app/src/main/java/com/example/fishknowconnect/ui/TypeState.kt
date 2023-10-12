package com.example.fishknowconnect.ui

sealed class TypeState {
    object None: TypeState()
    object Loading : TypeState()
    data class Success(val response: List<GetPostTypeResponse>?) : TypeState()
    data class Error(val response: List<GetPostTypeResponse>?) : TypeState()
    data class Failure(val string: String) : TypeState()

}