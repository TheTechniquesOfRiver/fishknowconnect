package com.example.fishknowconnect.ui.approvePostRequest

/**
 * Response data for get all post
 */
data class GetApprovalResponse(
    val _id: String,
    val author: String,
    val content: String,
    val fileType: String,
    val requested:String,
    val timestamp: String,
    val title: String,
    val type: String
)

//{
//    "_id": "65279829ce53f83fb8077284",
//    "access": "private",
//    "author": "11",
//    "content": "",
//    "fileType": "",
//    "requested": "Test6,12,",
//    "timestamp": "Thu, 12 Oct 2023 06:54:33 GMT",
//    "title": "t2",
//    "type": "Fish"
//}