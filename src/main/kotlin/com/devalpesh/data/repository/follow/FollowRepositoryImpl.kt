package com.devalpesh.data.repository.follow

import com.devalpesh.data.models.Following
import com.devalpesh.data.models.User
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class FollowRepositoryImpl(
    db: CoroutineDatabase
) : FollowRepository {

    private val following = db.getCollection<Following>()
    private val user = db.getCollection<User>()

    override suspend fun followUserIfExists(
        followingUserId: String,
        followedUserId: String
    ): Boolean {
        val doesFollowingUserExists = user.findOneById(followingUserId) != null
        val doesFollowedUserExists = user.findOneById(followedUserId) != null
        if (!doesFollowingUserExists || !doesFollowedUserExists) {
            return false
        }
        following.insertOne(
            Following(followingUserId, followedUserId)
        )
        return true
    }

    override suspend fun unfollowedUserIfExists(followingUserId: String, followedUserId: String): Boolean {
        val deleteResult = following.deleteOne(
            and(
                Following::followingUserId eq followingUserId,
                Following::followedUserId eq followedUserId,
            )
        )
        return deleteResult.deletedCount > 0
    }

}