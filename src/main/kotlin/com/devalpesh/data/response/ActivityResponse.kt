package com.devalpesh.data.response

data class ActivityResponse(
    val timestamp: Long,
    val userId: String,
    val parentId: String,
    val type: Int,
    val username:String,
    val id: String
)
