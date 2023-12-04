package com.example.fishknowconnect.ui.home

sealed class HomeState {
    object None: HomeState()
    object Loading : HomeState()
    data class Success(val response: HomeApprovalCountResponse?) : HomeState()
    data class Error(val response: HomeApprovalCountResponse?) : HomeState()
    data class Failure(val string: String) : HomeState()

}