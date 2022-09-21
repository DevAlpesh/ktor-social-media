package com.devalpesh.controller.user

import com.devalpesh.data.models.User

interface UserController {

    suspend fun createUser(user: User)

    suspend fun getUserById(id: String): User?

    suspend fun getUserByEmail(email : String) : User?

}