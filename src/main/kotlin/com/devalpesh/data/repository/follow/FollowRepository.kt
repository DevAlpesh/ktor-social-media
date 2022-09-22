package com.devalpesh.data.repository.follow

interface FollowRepository {

    suspend fun followUserIfExists(
        followingUserId: String,
        followedUserId: String
    ): Boolean


    suspend fun unfollowedUserIfExists(
        followingUserId: String,
        followedUserId: String
    ): Boolean

}