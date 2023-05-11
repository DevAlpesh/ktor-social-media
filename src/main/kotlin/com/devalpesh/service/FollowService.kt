package com.devalpesh.service

import com.devalpesh.data.repository.follow.FollowRepository
import com.devalpesh.data.request.FollowUpdateRequest

class FollowService(
    val repository: FollowRepository
) {
    suspend fun followUserIfExists(request: FollowUpdateRequest, followingUserId: String): Boolean {
        return repository.followUserIfExists(
            followingUserId,
            request.followedUserId
        )
    }

    suspend fun unFollowUserIfExists(followedUserId: String, followingUserId: String): Boolean {
        return repository.unfollowedUserIfExists(
            followingUserId,
            followedUserId
        )
    }
}