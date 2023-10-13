package com.example.fishknowconnect.ui.boat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fishknowconnect.PreferenceHelper
import com.example.fishknowconnect.network.FishKnowConnectApi
import com.example.fishknowconnect.network.FishKnowConnectApiService
import com.example.fishknowconnect.ui.TypeState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.ProtocolException

class BoatViewModel(preferenceHelper: PreferenceHelper, private val retrofitService: FishKnowConnectApiService) : ViewModel() {
    private val mutableState = MutableStateFlow<TypeState>(TypeState.None)
    val state = mutableState.asStateFlow()
    var username = preferenceHelper.getLoggedInUsernameUser()

    /**
     * fetch all content data
     */
    fun getAllBoatContents() {
        viewModelScope.launch(Dispatchers.Main) {
            mutableState.value = TypeState.Loading
            try {
                val response =
                    retrofitService.getPostsByType(username,"Boat")
                val boatResponse = response.body()
                if (boatResponse.isNullOrEmpty()) {
                    mutableState.value = TypeState.Failure("empty response")
                } else {
                    if (response.isSuccessful) {
                        when (response.code()) {
                            200  -> mutableState.value = TypeState.Success(boatResponse)
                        }
                    } else {
                        when (response.code()) {
                            409 -> mutableState.value = TypeState.Error(boatResponse)
                        }
                    }
                }
            }catch (_: ProtocolException){

            }

        }
    }
}