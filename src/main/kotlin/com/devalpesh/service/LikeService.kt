package com.devalpesh.service

import com.devalpesh.data.models.User
import com.devalpesh.data.repository.follow.FollowRepository
import com.devalpesh.data.repository.likes.LikesRepository
import com.devalpesh.data.repository.user.UserRepository
import com.devalpesh.data.response.UserResponseItem
import com.devalpesh.util.Constant

class LikeService(
    private val likeRepository: LikesRepository,
    private val userRepository: UserRepository,
    private val followRepository: FollowRepository
) {
    suspend fun likeParent(userId: String, parentId: String, parentType: Int): Boolean {
        return likeRepository.likeParent(userId, parentId, parentType)
    }

    suspend fun unlikeParent(userId: String, parentId: String): Boolean {
        return likeRepository.unlikeParent(userId, parentId)
    }

    suspend fun deleteLikesForParent(parentId: String) {
        likeRepository.deleteLikesForParents(parentId)
    }

    suspend fun getUserWhoLikesParent(
        parentId: String,
        userId: String,
        page: Int = 0,
        pageSize: Int = Constant.DEFAULT_POST_PAGE_SIZE
    ): List<UserResponseItem> {

        val userIds = likeRepository.getLikesForParent(parentId, page, pageSize).map { it.userId }
        val users = userRepository.getUsers(userIds)
        val followsByUser = followRepository.getFollowsByUser(userId)
        return users.map { user: User ->
            val isFollowing = followsByUser.find { it.followedUserId == userId } != null
            UserResponseItem(
                username = user.username,
                profilePictureUrl = user.profileImageUrl,
                bio = user.bio,
                isFollowing = isFollowing
            )
        }
    }

}