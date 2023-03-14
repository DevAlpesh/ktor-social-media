package com.devalpesh.repository.user

import com.devalpesh.data.models.User
import com.devalpesh.data.repository.user.UserRepository
import com.devalpesh.data.response.UpdateProfileRequest

class FakeUserRepository : UserRepository {

    private val users = mutableListOf<User>()

    override suspend fun createUser(user: User) {
        users.add(user)
    }

    override suspend fun getUserById(id: String): User? {
        return users.find { it.id == id }
    }

    override suspend fun getUserByEmail(email: String): User? {
        return users.find { it.email == email }
    }

    override suspend fun updateUser(
        userId: String,
        profileImageUrl: String,
        updateProfileRequest: UpdateProfileRequest
    ): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun doesPasswordForUserMatch(email: String, enteredPassword: String): Boolean {
        val user = getUserByEmail(email)
        return user?.password == enteredPassword
    }

    override suspend fun doesEmailBelongToUserId(email: String, userId: String): Boolean {
       // TODO("Not yet implemented")
        return false
    }

    override suspend fun searchForUser(query: String): List<User> {
        TODO("Not yet implemented")
    }

}
