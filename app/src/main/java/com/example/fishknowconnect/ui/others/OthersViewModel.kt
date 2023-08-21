package com.example.fishknowconnect.ui.fish

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OthersViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is others "
    }
    val text: LiveData<String> = _text
}