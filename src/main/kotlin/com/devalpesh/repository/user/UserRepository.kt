package com.devalpesh.repository.user

import com.devalpesh.data.models.User

interface UserRepository {

    suspend fun createUser(user: User)

    suspend fun getUserById(id: String): User?

    suspend fun getUserByEmail(email : String) : User?

}