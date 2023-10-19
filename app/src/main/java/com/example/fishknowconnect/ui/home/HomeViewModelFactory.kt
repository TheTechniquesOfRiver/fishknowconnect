package com.example.fishknowconnect.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fishknowconnect.PreferenceHelper
import com.example.fishknowconnect.network.FishKnowConnectApi
import com.example.fishknowconnect.network.FishKnowConnectApiService

class HomeViewModelFactory(private val preferenceHelper: PreferenceHelper,
                           private val retrofitService: FishKnowConnectApiService,) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeApprovalCountViewModel::class.java)) {
            return HomeApprovalCountViewModel(preferenceHelper,retrofitService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}