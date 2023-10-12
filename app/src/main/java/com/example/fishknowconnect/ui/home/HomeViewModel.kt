package com.example.fishknowconnect.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fishknowconnect.network.FishKnowConnectApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.ProtocolException

class HomeViewModel : ViewModel() {
    private val mutableState = MutableStateFlow<HomeState>(HomeState.None)
    val state = mutableState.asStateFlow()

    /**
     * fetch all content data
     */
//    fun getAllFishContents() {
//        viewModelScope.launch(Dispatchers.Main) {
//            mutableState.value = HomeState.Loading
//            try {
//                val response =
//                    FishKnowConnectApi.retrofitService.getPostsByType("Fish")
//                val homeResponse = response.body()
//                if (homeResponse.isNullOrEmpty()) {
//                    mutableState.value = HomeState.Failure("empty response")
//                } else {
//                    if (response.isSuccessful) {
//                        when (response.code()) {
//                            200  -> mutableState.value = HomeState.Success(homeResponse)
//                        }
//                    } else {
//                        when (response.code()) {
//                            409 -> mutableState.value = HomeState.Error(homeResponse)
//                        }
//                    }
//                }
//            }catch (_: ProtocolException){
//
//            }
//
//        }
//    }
}