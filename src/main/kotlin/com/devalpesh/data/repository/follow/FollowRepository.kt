package com.devalpesh.data.repository.follow

import com.devalpesh.data.models.Following

interface FollowRepository {

    suspend fun followUserIfExists(
        followingUserId: String,
        followedUserId: String
    ): Boolean

    suspend fun getFollowsByUser(userId : String) : List<Following>

    suspend fun unfollowedUserIfExists(
        followingUserId: String,
        followedUserId: String
    ): Boolean

    suspend fun doesUserFollow(followingUserId: String,followedUserId: String) : Boolean

}