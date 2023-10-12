package com.example.fishknowconnect.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fishknowconnect.PreferenceHelper
import com.example.fishknowconnect.network.FishKnowConnectApi
import com.example.fishknowconnect.network.FishKnowConnectApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.ProtocolException

class ProfileViewModel(
    preferenceHelper: PreferenceHelper, private val retrofitService: FishKnowConnectApiService
) : ViewModel() {
    private val mutableState = MutableStateFlow<ProfileState>(ProfileState.None)
    private val mutableStateProfilePost =
        MutableStateFlow<ProfilePostListPostState>(ProfilePostListPostState.None)
    val state = mutableState.asStateFlow()
    val stateProfile = mutableStateProfilePost.asStateFlow()
    var username = preferenceHelper.getLoggedInUsernameUser()

    /**
     * fetch profile data
     */
    fun getProfileInfo() {
        viewModelScope.launch(Dispatchers.Main) {
            mutableState.value = ProfileState.Loading
            try {
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
            } catch (_: ProtocolException) {

            }
        }
    }

    /**
     * fetch all profile post list data
     */
    fun getProfilePosts() {
        viewModelScope.launch(Dispatchers.Main) {
            mutableStateProfilePost.value = ProfilePostListPostState.Loading
            try {
                val response = FishKnowConnectApi.retrofitService.getProfilePosts(username)
                val profilePostResponse = response.body()
                if (profilePostResponse.isNullOrEmpty()) {
                    mutableStateProfilePost.value = ProfilePostListPostState.Failure("empty response")
                } else {
                    if (response.isSuccessful) {
                        when (response.code()) {
                            200 -> mutableStateProfilePost.value =
                                ProfilePostListPostState.Success(profilePostResponse)
                        }
                    } else {
                        when (response.code()) {
//                            409 -> mutableStateProfilePost.value =
//                                ProfilePostListPostState.Error(profilePostResponse)
                        }
                    }
                }
            } catch (_: ProtocolException) {

            }

        }
    }
}

