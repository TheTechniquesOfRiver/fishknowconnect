package com.example.fishknowconnect.ui.newPost

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fishknowconnect.network.FishKnowConnectApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class NewPostViewModel() : ViewModel() {
    private val mutableState = MutableStateFlow<NewPostState>(NewPostState.None)
    val state = mutableState.asStateFlow()

    //password update
    var content by mutableStateOf("")
        private set

    fun updateContent(input: String) {
        content = input
    }

    var postType by mutableStateOf("")
        private set

    fun updateType(type: String) {
        postType = type
    }

    var file by mutableStateOf(File(""))
        private set

    fun updateFile(uploadedFile: File) {
        file = uploadedFile
    }

    /**
     * Converts file into multipart form for post
     */
    private fun changeFileIntoMultiPartForm(): MultipartBody.Part {
        val mediaType: String = "image/*"
        val reqFile = file.asRequestBody(mediaType.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("file", file.name, reqFile)
    }

    /**
     * upload image tp server
     *@param title title of the content
     * @param type type of the content
     */
    fun uploadPictureToServer(title: String, type: String) {
        viewModelScope.launch(Dispatchers.Main) {
            mutableState.value = NewPostState.Success("success")
            mutableState.value = NewPostState.Loading
            val response = FishKnowConnectApi.retrofitService.createPost(
                title, type, content, changeFileIntoMultiPartForm()
            )
            val newPostResponse = response.body()
            if (newPostResponse == null) {
                mutableState.value = NewPostState.Error("response null value")
            } else {
                if (response.isSuccessful) {
                    when (response.code()) {
                        201 -> mutableState.value = NewPostState.Success(newPostResponse.message)
                    }
                } else {
                    when (response.code()) {
                        409 -> mutableState.value = NewPostState.Error(newPostResponse.message)
                    }
                }
            }

        }
    }
}