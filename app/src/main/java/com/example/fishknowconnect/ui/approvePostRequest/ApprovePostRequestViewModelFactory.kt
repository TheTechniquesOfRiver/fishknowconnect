package com.example.fishknowconnect.ui.approvePostRequest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fishknowconnect.PreferenceHelper
import com.example.fishknowconnect.network.FishKnowConnectApiService

class ApprovePostRequestViewModelFactory(
    private val preferenceHelper: PreferenceHelper,
    private val retrofitService: FishKnowConnectApiService,
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ApprovePostRequestViewModel(preferenceHelper, retrofitService) as T
    }
}