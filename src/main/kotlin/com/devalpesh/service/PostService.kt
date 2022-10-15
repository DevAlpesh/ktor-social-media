package com.devalpesh.service

import com.devalpesh.data.models.Post
import com.devalpesh.data.repository.post.PostRepository
import com.devalpesh.data.request.CreatePostRequest
import com.devalpesh.util.Constant

class PostService(
    private val repository: PostRepository
) {

    suspend fun createPostIfUserExists(request: CreatePostRequest, userId: String): Boolean {
        return repository.createPostIfUserExists(
            Post(
                imageUrl = "",
                userId = userId,
                timestamp = System.currentTimeMillis(),
                description = request.description
            )
        )
    }

    suspend fun getPostForFollows(
        userId: String,
        page: Int = 0,
        pageSize: Int = Constant.DEFAULT_POST_PAGE_SIZE
    ): List<Post> {
        return repository.getPostByFollows(
            userId = userId,
            page = page,
            pageSize = pageSize
        )
    }

    suspend fun getPostForProfile(
        userId: String,
        page: Int = 0,
        pageSize: Int = Constant.DEFAULT_POST_PAGE_SIZE
    ): List<Post> {
        return repository.getPostForProfile(
            userId = userId,
            page = page,
            pageSize = pageSize
        )
    }

    suspend fun getPost(postId: String): Post? {
        return repository.getPost(postId)
    }

    suspend fun deletePost(postId: String) {
        return repository.deletePost(postId)
    }

}