package com.devalpesh.data.models

import org.bson.codecs.pojo.annotations.BsonId

data class Following(
    @BsonId
    val id: String = Object().toString(),
    val followingUserId: String,
    val followedUserId: String,
)
