package com.example.fishknowconnect.ui.setting

import androidx.compose.ui.res.stringResource
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fishknowconnect.R

class SettingViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Change language \n দয়া  করে  ভাষা পছন্দ করুন "
    }
    val text: LiveData<String> = _text
}