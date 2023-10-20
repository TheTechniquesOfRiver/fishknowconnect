package com.example.fishknowconnect.ui.privatePost

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fishknowconnect.PreferenceHelper
import com.example.fishknowconnect.network.FishKnowConnectApi
import com.example.fishknowconnect.network.FishKnowConnectApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.ProtocolException

class PrivatePostViewModel(
    preferenceHelper: PreferenceHelper, private val retrofitService: FishKnowConnectApiService
) : ViewModel() {
    private val mutableState = MutableStateFlow<PrivatePostState>(PrivatePostState.None)
    val state = mutableState.asStateFlow()
    var username = preferenceHelper.getLoggedInUsernameUser()

    /**
     * fetch all content data
     */
    fun getAllPrivatePostContent(type: String) {
        viewModelScope.launch(Dispatchers.Main) {
            mutableState.value = PrivatePostState.Loading
            try {
                val response = retrofitService.getAllPrivatePost(username, type)
                val privatePostResponse = response.body()
                if (privatePostResponse.isNullOrEmpty()) {
                    mutableState.value = PrivatePostState.Failure("empty response")
                } else {
                    if (response.isSuccessful) {
                        when (response.code()) {
                            200 -> mutableState.value =
                                PrivatePostState.Success(privatePostResponse)
                        }
                    } else {
                        when (response.code()) {
                            409 -> mutableState.value = PrivatePostState.Error(privatePostResponse)
                        }
                    }
                }
            } catch (_: ProtocolException) {

            }

        }
    }

    /**
     * send request to access private post
     */

    fun sendRequestToAccessPost(id: String) {
        viewModelScope.launch(Dispatchers.Main) {
            mutableState.value = PrivatePostState.Loading
            try {
                val response = retrofitService.sendPostAccessRequest(id, username.toRequestBody())
                val accessResponse = response.body()
//            if (privatePostResponse.isNullOrEmpty()) {
//                mutableState.value = PrivatePostState.Failure("empty response")
                if (response.isSuccessful) {
                    when (response.code()) {
                        200 -> if (accessResponse != null) {
                            mutableState.value =
                                PrivatePostState.SuccessAccess(accessResponse.message)
                        }

                        204 -> if (accessResponse != null) {
                            mutableState.value = PrivatePostState.Failure(accessResponse.message)
                        }
                    }
                } else {
                    when (response.code()) {
                        404 -> if (accessResponse != null) {
                            mutableState.value = PrivatePostState.Failure(accessResponse.message)
                        }
                    }
                }
            } catch (_: ProtocolException) {

            }

        }
    }
}