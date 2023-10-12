package com.example.fishknowconnect.ui.contentDetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fishknowconnect.network.FishKnowConnectApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.ProtocolException

class DeleteContentViewModel : ViewModel() {
    private val mutableState = MutableStateFlow<DeleteContentState>(DeleteContentState.None)
    val state = mutableState.asStateFlow()

    /**
     * fetch all content data
     */
    fun deleteContent(intentId: String) {
        viewModelScope.launch(Dispatchers.Main) {
            mutableState.value = DeleteContentState.Loading
            try {
                val response = FishKnowConnectApi.retrofitService.deletePost(intentId)
                if (response.isSuccessful) {
                    val deleteResponse = response.body()
                    if (deleteResponse != null) {
                        when (response.code()) {
                            200 -> mutableState.value = DeleteContentState.Success(deleteResponse)
                            409 -> mutableState.value = DeleteContentState.Error(deleteResponse)
                        }
                    } else {
                        mutableState.value = DeleteContentState.Failure("Null response")
                    }
                } else {
                    mutableState.value = DeleteContentState.Failure("Error response")
                }
            } catch (_: ProtocolException) {

            }

        }
    }
}