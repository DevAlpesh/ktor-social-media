package com.devalpesh.data.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Activity(
    val timestamp: Long,
    val byUserId: String,
    val toUserId: String,
    val parentId: String,
    val type: Int,
    @BsonId
    val id: String = ObjectId().toString()
)
