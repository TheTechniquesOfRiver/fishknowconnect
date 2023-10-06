package com.example.fishknowconnect.ui.privatePost

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fishknowconnect.network.FishKnowConnectApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PrivatePostViewModel : ViewModel() {
    private val mutableState = MutableStateFlow<PrivatePostState>(PrivatePostState.None)
    val state = mutableState.asStateFlow()

    /**
     * fetch all content data
     */
    fun getAllPrivatePostContent() {
        viewModelScope.launch(Dispatchers.Main) {
            mutableState.value = PrivatePostState.Loading
            val response =
                FishKnowConnectApi.retrofitService.getAllPrivatePost()
            val privatePostResponse = response.body()
            if (privatePostResponse.isNullOrEmpty()) {
                mutableState.value = PrivatePostState.Failure("empty response")
            } else {
                if (response.isSuccessful) {
                    when (response.code()) {
                        200  -> mutableState.value = PrivatePostState.Success(privatePostResponse)
                    }
                } else {
                    when (response.code()) {
                        409 -> mutableState.value = PrivatePostState.Error(privatePostResponse)
                    }
                }
            }

        }
    }

    /**
     * send request to access private post
     */

    fun sendRequestToAccessPost(_id: String) {
        viewModelScope.launch(Dispatchers.Main) {
            mutableState.value = PrivatePostState.Loading
            val response =
                FishKnowConnectApi.retrofitService.sendPostAccessRequest(_id)
//            val privatePostAccessResponse = response.body()
//            if (privatePostAccessResponse.isNullOrEmpty()) {
//                mutableState.value = PrivatePostState.Failure("empty response")
//            } else {
//                if (response.isSuccessful) {
//                    when (response.code()) {
//                        200  -> mutableState.value = PrivatePostState.Success(privatePostResponse)
//                    }
//                } else {
//                    when (response.code()) {
//                        409 -> mutableState.value = PrivatePostState.Error(privatePostResponse)
//                    }
//                }
//            }

        }
    }
}