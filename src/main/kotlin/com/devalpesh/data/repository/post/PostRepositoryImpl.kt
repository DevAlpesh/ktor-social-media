package com.devalpesh.data.repository.post

import com.devalpesh.data.models.Following
import com.devalpesh.data.models.Like
import com.devalpesh.data.models.Post
import com.devalpesh.data.models.User
import com.devalpesh.data.response.PostResponse
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.`in`

class PostRepositoryImpl(
    db: CoroutineDatabase
) : PostRepository {

    private val posts = db.getCollection<Post>()
    private val following = db.getCollection<Following>()
    private val users = db.getCollection<User>()
    private val likes = db.getCollection<Like>()

    override suspend fun createPost(post: Post): Boolean {
        return posts.insertOne(post).wasAcknowledged()
    }

    override suspend fun deletePost(postId: String) {
        posts.deleteOneById(postId)
    }

    override suspend fun getPostByFollows(
        userId: String,
        page: Int,
        pageSize: Int
    ): List<Post> {
        val userIdsFromFollows = following.find(Following::followingUserId eq userId)
            .toList()
            .map {
                it.followedUserId
            }
        return posts.find(Post::userId `in` userIdsFromFollows)
            .skip(page * pageSize)
            .limit(pageSize)
            .descendingSort(Post::timestamp)
            .toList()
    }

    override suspend fun getPostForProfile(
        ownUserId: String,
        userId: String,
        page: Int,
        pageSize: Int
    ): List<PostResponse> {
        val user = users.findOneById(userId) ?: return emptyList()
        return posts.find(Post::userId eq userId)
            .skip(page * pageSize)
            .limit(pageSize)
            .descendingSort(Post::timestamp)
            .toList()
            .map { post ->
                val isLiked = likes.findOne(
                    and(
                        Like::parentId eq post.id,
                        Like::userId eq ownUserId
                    )
                ) != null
                PostResponse(
                    id = post.id,
                    userId = post.userId,
                    username = user.username,
                    imageUrl = post.imageUrl,
                    profilePictureUrl = user.profileImageUrl,
                    description = post.description,
                    likeCount = post.likeCount,
                    commentCount = post.commentCount,
                    isLiked = isLiked
                )
            }
    }

    override suspend fun getPost(postId: String): Post? {
        return posts.findOneById(postId)
    }

    override suspend fun getPostDetails(userId: String, postId: String): PostResponse? {
        val isLiked = likes.findOne(Like::userId eq userId) != null
        val post = posts.findOneById(postId) ?: return null
        val user = users.findOneById(post.userId) ?: return null
        return PostResponse(
            id = post.id,
            username = user.username,
            userId = post.userId,
            imageUrl = post.imageUrl,
            profilePictureUrl = user.profileImageUrl,
            description = post.description,
            likeCount = post.likeCount,
            commentCount = post.commentCount,
            isLiked = isLiked
        )
    }
}