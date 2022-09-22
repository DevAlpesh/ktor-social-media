package com.devalpesh.data.request

data class FollowUpdateRequest(
    val followingUserId: String,
    val followedUserId: String,
)