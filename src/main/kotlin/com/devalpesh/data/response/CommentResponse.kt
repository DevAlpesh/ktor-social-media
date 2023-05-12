package com.devalpesh.data.response

data class CommentResponse(
    val id: String,
    val username: String,
    val profilePictureUrl: String,
    val timestamp: Long,
    val comment: String,
    val likeCount: Int,
    val isLiked: Boolean
)