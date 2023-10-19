package com.example.fishknowconnect.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

class HomeApprovalCountViewModel(
    preferenceHelper: PreferenceHelper,
    private val retrofitService: FishKnowConnectApiService
) : ViewModel() {
    private val mutableState = MutableStateFlow<HomeState>(HomeState.None)
    val state = mutableState.asStateFlow()
    var username = preferenceHelper.getLoggedInUsernameUser()
    private var _onSuccess = MutableLiveData<String>()
    val onSuccess: LiveData<String>
        get() = _onSuccess

    private var _onFailure = MutableLiveData<String>()
    val onFailure: LiveData<String>
        get() = _onFailure

    /**
     * fetchcount
     */
    fun getApprovalCount() {
        viewModelScope.launch(Dispatchers.Main) {
            mutableState.value = HomeState.Loading
            try {
                val response =
                    retrofitService.getApprovalRequestsCount(username)
                Log.d("home", "home"+response.body())
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        _onSuccess.value = response.body()!!.total_requests
                    }else{
                        _onFailure.value = "0"
                    }
                } else {
                    _onFailure.value = "0"
                }
            } catch (_: ProtocolException) {

            }

        }
    }
}