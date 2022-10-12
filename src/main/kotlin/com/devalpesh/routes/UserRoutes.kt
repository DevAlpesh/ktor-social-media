package com.devalpesh.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.devalpesh.data.request.LoginRequest
import com.devalpesh.data.request.CreateAccountRequest
import com.devalpesh.data.response.ApiResponseMessages.FIELDS_BLANK
import com.devalpesh.data.response.ApiResponseMessages.INVALID_CREDENTIALS
import com.devalpesh.data.response.ApiResponseMessages.USER_ALREADY_EXIST
import com.devalpesh.data.response.AuthResponse
import com.devalpesh.data.response.BasicApiResponse
import com.devalpesh.service.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Route.createUser(
    service: UserService
) {
    post("/api/user/create") {
        val request = call.receiveNullable<CreateAccountRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        if (service.doesUserWithEmailExists(request.email)) {
            call.respond(
                BasicApiResponse(
                    success = false, message = USER_ALREADY_EXIST
                )
            )
            return@post
        }

        when (service.validateCreateAccountRequest(request)) {
            is UserService.ValidationEvents.ErrorFieldEmpty -> {
                call.respond(
                    BasicApiResponse(
                        success = false, message = FIELDS_BLANK
                    )
                )
            }

            is UserService.ValidationEvents.SuccessEvent -> {
                service.createUser(request)
                call.respond(
                    BasicApiResponse(success = true)
                )
            }
        }
    }
}

fun Route.loginUser(
    userService: UserService,
    jwtIssuer: String,
    jwtAudience: String,
    jwtSecret: String
) {
    post("/api/user/login") {
        val request = call.receiveNullable<LoginRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        if (request.email.isBlank() || request.password.isBlank()) {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val isCorrectPassword = userService.doesPasswordForUserMatch(request)
        if (isCorrectPassword) {

            val expiresIn = 1000L * 60L * 60L * 24L * 365L

            val token = JWT.create()
                .withClaim("email", request.email)
                .withIssuer(jwtIssuer)
                .withExpiresAt(Date(System.currentTimeMillis() + expiresIn))
                .withAudience(jwtAudience)
                .sign(Algorithm.HMAC256(jwtSecret))
            call.respond(
                HttpStatusCode.OK,
                AuthResponse(token = token)
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