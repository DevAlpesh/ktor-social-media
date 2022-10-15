package com.devalpesh.data.response

data class ProfileResponse(
    val username: String,
    val bio: String,
    val followingCount: Int,
    val followerCount: Int,
    val postCount: Int,
    val profilePictureUrl: String,
    val topSkillLinks: List<String>,
    val githubUrl: String?,
    val instagramUrl: String?,
    val linkedInUrl: String?,
    val isFollowing: Boolean,
    val isOwnProfile: Boolean
)