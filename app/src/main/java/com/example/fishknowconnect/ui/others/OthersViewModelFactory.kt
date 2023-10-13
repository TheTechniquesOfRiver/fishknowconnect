package com.example.fishknowconnect.ui.others

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fishknowconnect.PreferenceHelper
import com.example.fishknowconnect.network.FishKnowConnectApiService

class OthersViewModelFactory(
    private val preferenceHelper: PreferenceHelper,
    private val retrofitService: FishKnowConnectApiService,
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OthersViewModel(preferenceHelper, retrofitService) as T
    }
}