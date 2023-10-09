package com.example.fishknowconnect.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fishknowconnect.PreferenceHelper
import com.example.fishknowconnect.network.FishKnowConnectApiService

class ProfileViewModelFactory(
    private val preferenceHelper: PreferenceHelper,
    private val retrofitService: FishKnowConnectApiService,
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileViewModel(preferenceHelper, retrofitService) as T
    }
}