package com.example.fishknowconnect.ui.fish

/**
 * Response data for get all post
 */
data class GetAllPostResponse(
    val content: String,
    val fileUrl: String,
    val id: String,
    val title:String,
    val type: String
)