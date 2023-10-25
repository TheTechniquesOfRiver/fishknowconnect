package com.example.fishknowconnect.ui.approvePostRequest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fishknowconnect.PreferenceHelper
import com.example.fishknowconnect.network.FishKnowConnectApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.ProtocolException

class ApprovePostRequestViewModel(
    preferenceHelper: PreferenceHelper, private val retrofitService: FishKnowConnectApiService
) : ViewModel() {
    private val mutableState = MutableStateFlow<ApproveState>(ApproveState.None)
    val state = mutableState.asStateFlow()
    var username = preferenceHelper.getLoggedInUsernameUser()

    /**
     * fetch approvals data
     */
    fun getApprovals() {
        viewModelScope.launch(Dispatchers.Main) {
            mutableState.value = ApproveState.Loading
            try {
                val response = retrofitService.getApprovals(username)
                val approveResponse = response.body()
                if (approveResponse.isNullOrEmpty()) {
                    mutableState.value = ApproveState.Failure("empty response")
                } else {
                    if (response.isSuccessful) {
                        when (response.code()) {
                            200 -> mutableState.value = ApproveState.Success(approveResponse)
                        }
                    } else {
                        when (response.code()) {
                            409 -> mutableState.value = ApproveState.Error(approveResponse)
                        }
                    }
                }
            } catch (_: ProtocolException) {

            }

        }
    }


    /**
     * request for approval post
     */
    fun sendPostApproval(id: String, approvalRequestUser: String) {
        viewModelScope.launch(Dispatchers.Main) {
            mutableState.value = ApproveState.Loading
            try {
                val response =
                    retrofitService.sendGrantAccess(id, approvalRequestUser.toRequestBody())
                if (response.isSuccessful) {
                    val approveGrantResponse = response.body()
                    when (response.code()) {
                        200 -> mutableState.value = ApproveState.SuccessGrant(approveGrantResponse)
                    }
                } else {
                    when (response.code()) {
                        409 -> mutableState.value = ApproveState.Failure("failed")
                    }
                }
            } catch (_: ProtocolException) {
            }
        }
    }


/**
 * request for approval post
 */
fun sendRejectApproval(id: String, approvalRequestUser: String) {
    viewModelScope.launch(Dispatchers.Main) {
        mutableState.value = ApproveState.Loading
        try {
            val response =
                retrofitService.sendRejectApproval(id, approvalRequestUser.toRequestBody())
            if (response.isSuccessful) {
                val rejectGrantResponse = response.body()
                when (response.code()) {
                    200 -> mutableState.value = ApproveState.RejectGrant(rejectGrantResponse)
                }
            } else {
                when (response.code()) {
                    409 -> mutableState.value = ApproveState.Failure("failed")
                }
            }
        } catch (_: ProtocolException) {
        }
    }
}
}