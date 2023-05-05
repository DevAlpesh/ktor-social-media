package com.devalpesh.data.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class User(
    val email: String,
    val username: String,
    val password: String,
    val profileImageUrl: String,
    val bannerImageUrl: String,
    val bio: String,
    val githubUrl: String?,
    val instagramUrl: String?,
    val linkedInUrl: String?,
    val followerCount: Int = 0,
    val followingCount: Int = 0,
    val postCount: Int = 0,
    val topSkills: List<Skill> = listOf(),
    @BsonId
    val id: String = ObjectId().toString(),
)

