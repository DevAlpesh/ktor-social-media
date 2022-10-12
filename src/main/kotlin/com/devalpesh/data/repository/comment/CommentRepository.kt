package com.devalpesh.data.repository.comment

import com.devalpesh.data.models.Comment

interface CommentRepository {

    suspend fun createComment(comment: Comment)

    suspend fun deleteComment(commentId: String) : Boolean

    suspend fun getCommentsForPost(postId: String): List<Comment>

    suspend fun getComments(commentId: String): Comment?

}