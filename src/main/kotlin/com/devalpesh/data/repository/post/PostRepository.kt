package com.devalpesh.data.repository.post

import com.devalpesh.data.models.Post
import com.devalpesh.data.response.PostResponse
import com.devalpesh.util.Constant

interface PostRepository {

    suspend fun createPost(post: Post): Boolean

    suspend fun deletePost(postId: String)

    suspend fun getPostByFollows(
        ownUserId: String,
        page: Int = 0,
        pageSize: Int = Constant.DEFAULT_POST_PAGE_SIZE
    ): List<PostResponse>

    suspend fun getPostForProfile(
        ownUserId: String,
        userId: String,
        page: Int = 0,
        pageSize: Int = Constant.DEFAULT_POST_PAGE_SIZE
    ): List<PostResponse>

    suspend fun getPost(postId: String): Post?

    suspend fun getPostDetails(userId: String, postId: String): PostResponse?

}