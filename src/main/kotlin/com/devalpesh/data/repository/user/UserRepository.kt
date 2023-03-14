package com.devalpesh.data.repository.user

import com.devalpesh.data.models.User
import com.devalpesh.data.response.UpdateProfileRequest

interface UserRepository {
    suspend fun createUser(user: User)
    suspend fun getUserById(id: String): User?
    suspend fun getUserByEmail(email: String): User?
    suspend fun updateUser(userId: String, profileImageUrl: String, updateProfileRequest: UpdateProfileRequest): Boolean
    suspend fun doesPasswordForUserMatch(email: String, enteredPassword: String): Boolean
    suspend fun doesEmailBelongToUserId(email: String, userId: String): Boolean
    suspend fun searchForUser(query: String): List<User>
    suspend fun getUsers(userIds : List<String>) : List<User>
}