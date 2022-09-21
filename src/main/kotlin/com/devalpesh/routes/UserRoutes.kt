package com.devalpesh.routes

import com.devalpesh.data.repository.user.UserRepository
import com.devalpesh.data.models.User
import com.devalpesh.data.request.LoginRequest
import com.devalpesh.data.request.CreateAccountRequest
import com.devalpesh.data.response.ApiResponseMessages.FIELDS_BLANK
import com.devalpesh.data.response.ApiResponseMessages.INVALID_CREDENTIALS
import com.devalpesh.data.response.ApiResponseMessages.USER_ALREADY_EXIST
import com.devalpesh.data.response.BasicApiResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.createUserRoute(userRepository: UserRepository) {
    post("/api/user/create") {
        val request = call.receiveNullable<CreateAccountRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val userExist = userRepository.getUserByEmail(request.email) != null
        if (userExist) {
            call.respond(
                BasicApiResponse(
                    success = false, message = USER_ALREADY_EXIST
                )
            )
            return@post
        }
        if (request.email.isBlank() || request.password.isBlank() || request.username.isBlank()) {
            call.respond(
                BasicApiResponse(
                    success = false, message = FIELDS_BLANK
                )
            )
            return@post
        }
        userRepository.createUser(
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
        call.respond(
            BasicApiResponse(success = true)
        )
    }
}

fun Route.loginUser(userRepository: UserRepository) {
    post("/api/user/login") {
        val request = call.receiveNullable<LoginRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        if (request.email.isBlank() || request.password.isBlank()) {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val isCorrectPassword = userRepository.doesPasswordForUserMatch(
            email = request.email, enteredPassword = request.password
        )

        if (isCorrectPassword) {
            call.respond(
                HttpStatusCode.OK, BasicApiResponse(
                    success = true
                )
            )
        } else {
            call.respond(
                HttpStatusCode.OK, BasicApiResponse(
                    success = false, message = INVALID_CREDENTIALS
                )
            )
        }

    }
}