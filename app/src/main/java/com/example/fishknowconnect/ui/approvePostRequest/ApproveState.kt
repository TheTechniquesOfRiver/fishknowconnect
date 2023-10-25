package com.example.fishknowconnect.ui.approvePostRequest

import com.example.fishknowconnect.ui.privatePost.GetPrivatePostAccessResponse

sealed class ApproveState {
    object None: ApproveState()
    object Loading : ApproveState()
    data class Success(val response: List<GetApprovalResponse>?) : ApproveState()
    data class SuccessGrant(val response: GetPrivatePostAccessResponse?) : ApproveState()
    data class RejectGrant(val response: GetPrivatePostAccessResponse?) : ApproveState()
    data class Error(val response: List<GetApprovalResponse>?) : ApproveState()
    data class Failure(val string: String) : ApproveState()

}