package com.devalpesh.service

import com.devalpesh.data.repository.follow.FollowRepository
import com.devalpesh.data.request.FollowUpdateRequest

class FollowService(
    val repository: FollowRepository
) {
    suspend fun followUserIfExists(request: FollowUpdateRequest): Boolean {
        return repository.followUserIfExists(
            request.followingUserId,
            request.followedUserId
        )
    }

    suspend fun unFollowUserIfExists(request: FollowUpdateRequest): Boolean {
        return repository.unfollowedUserIfExists(
            request.followingUserId,
            request.followedUserId
        )
    }
}