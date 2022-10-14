package com.devalpesh.service

import com.devalpesh.data.models.Activity
import com.devalpesh.data.repository.activity.ActivityRepository
import com.devalpesh.data.repository.comment.CommentRepository
import com.devalpesh.data.repository.post.PostRepository
import com.devalpesh.data.util.ActivityType
import com.devalpesh.util.Constant
import com.devalpesh.data.util.ParentType

class ActivityService(
    private val activityRepository: ActivityRepository,
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository
) {
    suspend fun getActivitiesForUser(
        userId: String,
        page: Int,
        pageSize: Int = Constant.DEFAULT_ACTIVITY_PAGE_SIZE
    ): List<Activity> {
        return activityRepository.getActivitiesForUser(userId, page, pageSize)
    }

    suspend fun addLikeActivity(
        byUserId: String,
        parentType: ParentType,
        parentId: String
    ): Boolean {
        val toUserId = when (parentType) {
            is ParentType.Post -> {
                postRepository.getPost(parentId)?.userId
            }

            is ParentType.Comment -> {
                commentRepository.getComments(parentId)?.userId
            }

            is ParentType.None -> return false
        } ?: return false
        if (byUserId==toUserId){
            return false
        }
        activityRepository.createActivity(
            Activity(
                timestamp = System.currentTimeMillis(),
                byUserId = byUserId,
                toUserId = toUserId,
                type = when (parentType) {
                    is ParentType.Post -> ActivityType.LikedPost.type
                    is ParentType.Comment -> ActivityType.LikedComment.type
                    else -> ActivityType.LikedPost.type
                },
                parentId = parentId,
            )
        )
        return true
    }

    suspend fun addCommentActivity(
        byUserId: String,
        postId: String
    ): Boolean {
        val userIdOfPost = postRepository.getPost(postId)?.userId ?: return false
        if (byUserId == userIdOfPost){
            return false
        }
        activityRepository.createActivity(
            Activity(
                timestamp = System.currentTimeMillis(),
                byUserId = byUserId,
                toUserId = userIdOfPost,
                type = ActivityType.CommentedOnPost.type,
                parentId = postId
            )
        )
        return true
    }

    suspend fun createActivity(activity: Activity) {
        activityRepository.createActivity(activity)
    }

    suspend fun deleteActivity(activityId: String) {
        activityRepository.deleteActivity(activityId)
    }
}