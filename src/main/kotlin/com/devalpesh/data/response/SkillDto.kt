package com.devalpesh.data.response

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class SkillDto(
    val name: String,
    val imageUrl: String
)
