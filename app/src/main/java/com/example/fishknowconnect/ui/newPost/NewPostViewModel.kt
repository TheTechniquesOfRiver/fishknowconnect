package com.example.fishknowconnect.ui.newPost

import android.content.Context
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import java.util.Objects

class NewPostViewModel(context: Context) : ViewModel() {
    val imageFile = context.createImageFile()
}