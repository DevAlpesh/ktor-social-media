package com.devalpesh.data.repository.comment

import com.devalpesh.data.models.Comment
import com.devalpesh.data.response.CommentResponse

interface CommentRepository {

    suspend fun createComment(comment: Comment): String

    suspend fun deleteComment(commentId: String): Boolean

    suspend fun deleteCommentsFromPost(postId: String): Boolean

    suspend fun getCommentsForPost(postId: String, ownUserId: String): List<CommentResponse>

    suspend fun getComments(commentId: String): Comment?

}