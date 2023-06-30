package com.devalpesh.service

import com.devalpesh.data.models.User
import com.devalpesh.data.repository.follow.FollowRepository
import com.devalpesh.data.repository.user.UserRepository
import com.devalpesh.data.request.CreateAccountRequest
import com.devalpesh.data.response.ProfileResponse
import com.devalpesh.data.request.UpdateProfileRequest
import com.devalpesh.data.response.UserResponseItem
import com.devalpesh.util.Constant

class UserService(
    private val userRepository: UserRepository, private val followRepository: FollowRepository
) {

    suspend fun doesUserWithEmailExists(email: String): Boolean {
        return userRepository.getUserByEmail(email) != null
    }

    suspend fun getUserProfile(userId: String, callerUserId: String): ProfileResponse? {
        val user = userRepository.getUserById(userId) ?: return null
        return ProfileResponse(
            userId = user.id,
            username = user.username,
            bio = user.bio,
            followingCount = user.followingCount,
            followerCount = user.followerCount,
            postCount = user.postCount,
            profilePictureUrl = user.profileImageUrl,
            topSkills = user.topSkills,
            bannerUrl = user.bannerImageUrl,
            githubUrl = user.githubUrl,
            instagramUrl = user.instagramUrl,
            linkedInUrl = user.linkedInUrl,
            isOwnProfile = userId == callerUserId,
            isFollowing = if (userId != callerUserId) {
                followRepository.doesUserFollow(callerUserId, userId)
            } else {
                false
            }
        )
    }

    suspend fun getUserByEmail(email: String): User? {
        return userRepository.getUserByEmail(email)
    }

    fun isValidPassword(enteredPassword: String, actualPassword: String): Boolean {
        return enteredPassword == actualPassword
    }

    fun validateCreateAccountRequest(request: CreateAccountRequest): ValidationEvents {
        if (request.email.isBlank() || request.password.isBlank() || request.username.isBlank()) {
            return ValidationEvents.ErrorFieldEmpty
        }
        return ValidationEvents.SuccessEvent
    }

    suspend fun createUser(request: CreateAccountRequest) {
        userRepository.createUser(
            User(
                email = request.email,
                username = request.username,
                password = request.password,
                profileImageUrl = Constant.DEFAULT_PROFILE_PICTURE_PATH,
                bannerImageUrl = Constant.DEFAULT_BANNER_IMAGE_PATH,
                bio = "",
                githubUrl = "",
                instagramUrl = "",
                linkedInUrl = "",
            )
        )
    }

    sealed class ValidationEvents {
        object ErrorFieldEmpty : ValidationEvents()
        object SuccessEvent : ValidationEvents()
    }

    suspend fun updateUser(
        userId: String, profileImageUrl: String?, bannerImageUrl: String?, updateProfileRequest: UpdateProfileRequest
    ): Boolean {
        return userRepository.updateUser(userId, profileImageUrl, bannerImageUrl, updateProfileRequest)
    }

    suspend fun searchForUser(query: String, userId: String): List<UserResponseItem> {
        val users = userRepository.searchForUser(query)
        val followsByUser = followRepository.getFollowsByUser(userId)
        return users.map { user: User ->
            val isFollowing = followsByUser.find { it.followedUserId == user.id } != null
            UserResponseItem(
                userId = user.id,
                username = user.username,
                profilePictureUrl = user.profileImageUrl,
                bio = user.bio,
                isFollowing = isFollowing
            )
        }.filter { it.userId != userId }
    }
}










