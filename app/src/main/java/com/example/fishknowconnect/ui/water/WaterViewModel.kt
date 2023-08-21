package com.example.fishknowconnect.ui.fish

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WaterViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is water "
    }
    val text: LiveData<String> = _text
}