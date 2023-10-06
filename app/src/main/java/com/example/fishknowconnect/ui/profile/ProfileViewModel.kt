package com.example.fishknowconnect.ui.profile

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fishknowconnect.PreferenceHelper
import com.example.fishknowconnect.network.FishKnowConnectApi
import com.example.fishknowconnect.network.FishKnowConnectApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    preferenceHelper: PreferenceHelper,
    private val retrofitService: FishKnowConnectApiService
) : ViewModel() {
    private val mutableState = MutableStateFlow<ProfileState>(ProfileState.None)
    val state = mutableState.asStateFlow()
    var username = preferenceHelper.getLoggedInUsernameUser()

    /**
     * fetch profile data
     */
    fun getProfileInfo() {
        viewModelScope.launch(Dispatchers.Main) {
            mutableState.value = ProfileState.Loading
            val response = retrofitService.getProfileInfo(username)
            val profileResponse = response.body()
            if (response.isSuccessful) {
                when (response.code()) {
                    200 -> if (profileResponse != null) {
                        mutableState.value =
                            ProfileState.Success(profileResponse.age, profileResponse.location)
                    }
                }
            } else {
                when (response.code()) {
                    409 -> if (profileResponse != null) {
//                            mutableState.value = ProfileState.Error(profileResponse.m)
                    }
                }
            }
        }

    }
}

