package com.devalpesh.data.response

data class BasicApiResponse<T>(
    val success: Boolean,
    val message: String? = "",
    val data: T? = null
)