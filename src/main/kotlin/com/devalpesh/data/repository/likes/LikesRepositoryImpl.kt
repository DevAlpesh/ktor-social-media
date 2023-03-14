package com.devalpesh.data.repository.likes

import com.devalpesh.data.models.Like
import com.devalpesh.data.models.Post
import com.devalpesh.data.models.User
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class LikesRepositoryImpl(
    db: CoroutineDatabase
) : LikesRepository {

    private val like = db.getCollection<Like>()

    private val users = db.getCollection<User>()

    override suspend fun likeParent(userId: String, parentId: String, parentType: Int): Boolean {
        val doesUserExist = users.findOneById(userId) != null
        return if (doesUserExist) {
            like.insertOne(Like(userId, parentId, parentType,System.currentTimeMillis()))
            true
        } else {
            false
        }
    }

    override suspend fun unlikeParent(userId: String, parentId: String): Boolean {
        val doesUserExist = users.findOneById(userId) != null
        return if (doesUserExist) {
            like.deleteOne(
                and(
                    Like::userId eq userId,
                    Like::parentId eq parentId
                )
            )
            true
        } else {
            false
        }
    }

    override suspend fun deleteLikesForParents(parentId: String) {
        like.deleteMany(Like::parentId eq parentId)
    }

    override suspend fun getLikesForParent(parentId: String, page: Int, pageSize: Int): List<Like> {
        return like
            .find(Like::parentId eq parentId)
            .skip(page * pageSize)
            .limit(pageSize)
            .descendingSort(Like::timestamp)
            .toList()
    }


}