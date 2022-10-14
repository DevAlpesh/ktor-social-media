package com.devalpesh.data.response

data class UserResponseItem(
    val username: String,
    val profilePictureUrl: String,
    val bio: String,
    val isFollowing: Boolean,
)