package com.devalpesh.service

import com.devalpesh.data.models.Post
import com.devalpesh.data.repository.post.PostRepository
import com.devalpesh.data.request.CreatePostRequest
import com.devalpesh.data.response.PostResponse
import com.devalpesh.util.Constant

class PostService(
    private val repository: PostRepository
) {

    suspend fun createPost(request: CreatePostRequest, userId: String, imageUrl: String): Boolean {
        return repository.createPost(
            Post(
                imageUrl = imageUrl,
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
        ownUserId: String,
        userId: String,
        page: Int = 0,
        pageSize: Int = Constant.DEFAULT_POST_PAGE_SIZE
    ): List<PostResponse> {
        return repository.getPostForProfile(
            ownUserId = ownUserId,
            userId = userId,
            page = page,
            pageSize = pageSize
        )
    }

    suspend fun getPost(postId: String): Post? {
        return repository.getPost(postId)
    }

    suspend fun getPostDetails(ownUserId: String, postId: String): PostResponse? {
        return repository.getPostDetails(ownUserId, postId)
    }

    suspend fun deletePost(postId: String) {
        return repository.deletePost(postId)
    }

}