package com.example.fishknowconnect.ui

/**
 * Response data for get all post
 */
data class GetPostTypeResponse(
    val content: String,
    val file_url: String,
    val fileType: String,
    val _id: String,
    val title:String,
    val type: String
)