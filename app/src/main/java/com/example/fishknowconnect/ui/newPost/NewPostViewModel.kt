package com.example.fishknowconnect.ui.newPost

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fishknowconnect.PreferenceHelper
import com.example.fishknowconnect.network.FishKnowConnectApi
import com.example.fishknowconnect.network.FishKnowConnectApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class NewPostViewModel(
    private val preferenceHelper: PreferenceHelper,
    private  val retrofitService: FishKnowConnectApiService
) : ViewModel() {
    private val mutableState = MutableStateFlow<NewPostState>(NewPostState.None)
    val state = mutableState.asStateFlow()

    //password update
    var content by mutableStateOf("")
        private set

    fun updateContent(input: String) {
        content = input
    }
    //title update
    var postTitle by mutableStateOf("")
        private set

    fun updateTitle(title: String) {
        postTitle = title
    }

    var postType by mutableStateOf("")
        private set

    fun type(value: String) {
        postType = value
    }

    var file by mutableStateOf(File(""))
        private set

    fun updateFile(uploadedFile: File) {
            file = uploadedFile
    }

    var fileType by mutableStateOf("")
        private set

    fun updateFileType(type: String) {
        fileType = type
    }

    var access by mutableStateOf("")
        private set

    fun updateAccess(accessType: String) {
        access = accessType
    }
    /**
     * Converts file into multipart form for post
     */
    private fun changeFileIntoMultiPartForm(): MultipartBody.Part? {
        return if(file.exists()){
            val mediaType: String = "image/*"
            val reqFile = file.asRequestBody(mediaType.toMediaTypeOrNull())
            MultipartBody.Part.createFormData("file", file.name, reqFile)
        }else{
            null
        }
    }

    /**
     * upload image tp server
     *@param title title of the content
     * @param type type of the content
     */
    fun uploadPictureToServer(title: String) {
        viewModelScope.launch(Dispatchers.Main) {
            mutableState.value = NewPostState.Loading
            val response = retrofitService.createPost(
                postTitle.toRequestBody(),
                postType.toRequestBody(),
                content.toRequestBody(),
                changeFileIntoMultiPartForm(),
                fileType.toRequestBody(),
                access.toRequestBody(),
                preferenceHelper.getLoggedInUsernameUser().toRequestBody()
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

    /**
     * change string into text type requestBody
     * @return RequestBody
     */
    fun String.toRequestBody(): RequestBody {
        val mediaType = "text/plain"
        return toRequestBody(mediaType.toMediaTypeOrNull())
    }
}