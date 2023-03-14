package com.devalpesh.data.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.sql.Timestamp

data class Like(
    val userId: String,
    val parentId: String,
    val parentType : Int,
    val timestamp: Long,
    @BsonId
    val id: String = ObjectId().toString()
)
