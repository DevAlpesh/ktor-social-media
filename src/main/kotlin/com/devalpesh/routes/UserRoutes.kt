package com.devalpesh.routes

import com.devalpesh.repository.user.UserRepository
import com.devalpesh.data.models.User
import com.devalpesh.data.request.CreateAccountRequest
import com.devalpesh.data.response.ApiResponseMessages.FIELDS_BLANK
import com.devalpesh.data.response.ApiResponseMessages.USER_ALREADY_EXIST
import com.devalpesh.data.response.BasicApiResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.createUserRoute(userRepository: UserRepository) {

    route("/api/user/create") {
        post {
            val request = call.receiveNullable<CreateAccountRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            val userExist = userRepository.getUserByEmail(request.email) != null
            if (userExist) {
                call.respond(
                    BasicApiResponse(
                        success = false,
                        message = USER_ALREADY_EXIST
                    )
                )
                return@post
            }
            if (request.email.isBlank() || request.password.isBlank() || request.username.isBlank()) {
                call.respond(
                    BasicApiResponse(
                        success = false,
                        message = FIELDS_BLANK
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
}