package com.devalpesh.service

import com.devalpesh.data.models.Comment
import com.devalpesh.data.repository.comment.CommentRepository
import com.devalpesh.data.request.CreateCommentRequest
import com.devalpesh.util.Constant

class CommentService(
    private val repository: CommentRepository
) {
    suspend fun createComment(createCommentRequest: CreateCommentRequest): ValidationEvent {
        createCommentRequest.apply {
            if (comment.isBlank() || userId.isBlank() || postId.isBlank()) {
                return ValidationEvent.ErrorFieldEmpty
            }
            if (comment.length > Constant.MAX_COMMENT_LENGTH) {
                return ValidationEvent.CommentTooLong
            }
        }

        repository.createComment(
            Comment(
                comment = createCommentRequest.comment,
                userId = createCommentRequest.userId,
                postId = createCommentRequest.postId,
                timestamp = System.currentTimeMillis()
            )
        )
        return ValidationEvent.Success
    }

    suspend fun deleteComment(commentId: String): Boolean {
        return repository.deleteComment(commentId)
    }

    suspend fun getCommentsForPost(postId: String): List<Comment> {
        return repository.getCommentsForPost(postId)
    }

    sealed class ValidationEvent {
        object ErrorFieldEmpty : ValidationEvent()
        object CommentTooLong : ValidationEvent()
        object Success : ValidationEvent()
    }

}