package com.example.fishknowconnect.ui.weather

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fishknowconnect.network.FishKnowConnectApi
import com.example.fishknowconnect.ui.fish.FishState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.ProtocolException

class WeatherViewModel : ViewModel() {
    private val mutableState = MutableStateFlow<FishState>(FishState.None)
    val state = mutableState.asStateFlow()

    /**
     * fetch all content data
     */
    fun getAllWeatherContents() {
        viewModelScope.launch(Dispatchers.Main) {
            mutableState.value = FishState.Loading
            try {
                val response =
                    FishKnowConnectApi.retrofitService.getPostsByType("Weather")
                val fishResponse = response.body()
                if (fishResponse.isNullOrEmpty()) {
                    mutableState.value = FishState.Failure("empty response")
                } else {
                    if (response.isSuccessful) {
                        when (response.code()) {
                            200  -> mutableState.value = FishState.Success(fishResponse)
                        }
                    } else {
                        when (response.code()) {
                            409 -> mutableState.value = FishState.Error(fishResponse)
                        }
                    }
                }
            }catch (_: ProtocolException){

            }

        }
    }
}