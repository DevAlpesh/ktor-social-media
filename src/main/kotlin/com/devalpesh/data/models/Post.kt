package com.devalpesh.data.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Post(
    @BsonId
    val id: String = ObjectId().toString(),
    val imageUrl: String,
    val userId: String,
    val timestamp: Long,
    val description: String
)