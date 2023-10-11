package com.example.fishknowconnect.ui.others

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fishknowconnect.network.FishKnowConnectApi
import com.example.fishknowconnect.ui.TypeState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.ProtocolException

class OthersViewModel : ViewModel() {
    private val mutableState = MutableStateFlow<TypeState>(TypeState.None)
    val state = mutableState.asStateFlow()

    /**
     * fetch all content data
     */
    fun getAllOthersContents() {
        viewModelScope.launch(Dispatchers.Main) {
            mutableState.value = TypeState.Loading
            try {
                val response =
                    FishKnowConnectApi.retrofitService.getPostsByType("Others")
                val othersResponse = response.body()
                if (othersResponse.isNullOrEmpty()) {
                    mutableState.value = TypeState.Failure("empty response")
                } else {
                    if (response.isSuccessful) {
                        when (response.code()) {
                            200  -> mutableState.value = TypeState.Success(othersResponse)
                        }
                    } else {
                        when (response.code()) {
                            409 -> mutableState.value = TypeState.Error(othersResponse)
                        }
                    }
                }
            }catch (_: ProtocolException){

            }

        }
    }
}