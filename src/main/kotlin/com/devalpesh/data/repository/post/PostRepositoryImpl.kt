package com.devalpesh.data.repository.post

import com.devalpesh.data.models.Following
import com.devalpesh.data.models.Post
import com.devalpesh.data.models.User
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.`in`

class PostRepositoryImpl(
    db: CoroutineDatabase
) : PostRepository {

    private val posts = db.getCollection<Post>()
    private val following = db.getCollection<Following>()
    private val users = db.getCollection<User>()

    override suspend fun createPostIfUserExists(post: Post): Boolean {
        val doesUserExist = users.findOneById(post.userId) != null
        if (!doesUserExist) {
            return false
        }
        posts.insertOne(post)
        return true
    }

    override suspend fun deletePost(postId: String) {
        posts.deleteOneById(postId)
    }

    override suspend fun getPostByFollows(
        userId: String,
        page: Int,
        pageSize: Int
    ): List<Post> {
        val userIdsFromFollows = following.find(Following::followedUserId eq userId)
            .toList()
            .map {
                it.followingUserId
            }
        return posts.find(Post::userId `in` userIdsFromFollows)
            .skip(page * pageSize)
            .limit(pageSize)
            .descendingSort(Post::timestamp)
            .toList()
    }
}