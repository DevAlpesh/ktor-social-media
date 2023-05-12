package com.devalpesh.data.repository.likes

import com.devalpesh.data.models.Like
import com.devalpesh.util.Constant

interface LikesRepository {

    suspend fun likeParent(userId: String, parentId: String, parentType: Int): Boolean

    suspend fun unlikeParent(userId: String, parentId: String, parentType: Int): Boolean

    suspend fun deleteLikesForParents(parentId: String)

    suspend fun getLikesForParent(
        parentId: String,
        page: Int,
        pageSize: Int = Constant.DEFAULT_POST_PAGE_SIZE
    ): List<Like>
}