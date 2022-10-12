package com.devalpesh.data.request

data class CreateCommentRequest(
    val comment: String,
    val postId: String,
    val userId: String
)
