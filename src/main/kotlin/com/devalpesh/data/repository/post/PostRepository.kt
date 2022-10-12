package com.devalpesh.data.repository.post

import com.devalpesh.data.models.Post
import com.devalpesh.util.Constant

interface PostRepository {

    suspend fun createPostIfUserExists(post: Post): Boolean

    suspend fun deletePost(postId: String)

    suspend fun getPostByFollows(
        userId: String,
        page: Int = 0,
        pageSize: Int = Constant.DEFAULT_POST_PAGE_SIZE
    ): List<Post>

    suspend fun getPost(postId: String): Post?

}