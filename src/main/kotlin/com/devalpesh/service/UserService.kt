package com.devalpesh.service

import com.devalpesh.data.models.User
import com.devalpesh.data.repository.user.UserRepository
import com.devalpesh.data.request.CreateAccountRequest
import com.devalpesh.data.request.LoginRequest

class UserService(
    private val repository: UserRepository
) {

    suspend fun doesUserWithEmailExists(email: String): Boolean {
        return repository.getUserByEmail(email) != null
    }

    fun validateCreateAccountRequest(request: CreateAccountRequest): ValidationEvents {
        if (request.email.isBlank() || request.password.isBlank() || request.username.isBlank()) {
            return ValidationEvents.ErrorFieldEmpty
        }
        return ValidationEvents.SuccessEvent
    }

    suspend fun createUser(request: CreateAccountRequest) {
        repository.createUser(
            User(
                email = request.email,
                username = request.username,
                password = request.password,
                profileImageUrl = "",
                bio = "",
                githubUrl = "",
                instagramUrl = "",
                linkedInUrl = "",
            )
        )
    }

    sealed class ValidationEvents {
        object ErrorFieldEmpty : ValidationEvents()
        object SuccessEvent : ValidationEvents()
    }

    suspend fun doesPasswordForUserMatch(request: LoginRequest): Boolean {
        return repository.doesPasswordForUserMatch(
            email = request.email, enteredPassword = request.password
        )
    }


}