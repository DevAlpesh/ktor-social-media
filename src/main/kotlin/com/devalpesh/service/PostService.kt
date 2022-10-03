package com.devalpesh.service

import com.devalpesh.data.models.Post
import com.devalpesh.data.repository.post.PostRepository
import com.devalpesh.data.request.CreatePostRequest

class PostService(
    private val postRepository: PostRepository
) {

    suspend fun createPostIfUserExists(request: CreatePostRequest): Boolean {
        return postRepository.createPostIfUserExists(
            Post(
                imageUrl = "",
                userId = request.userId,
                timestamp = System.currentTimeMillis(),
                description = request.description
            )
        )
    }

}