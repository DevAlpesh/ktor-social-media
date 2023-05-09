package com.devalpesh.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.devalpesh.data.request.CreateAccountRequest
import com.devalpesh.data.request.LoginRequest
import com.devalpesh.data.response.ApiResponseMessages
import com.devalpesh.data.response.AuthResponse
import com.devalpesh.data.response.BasicApiResponse
import com.devalpesh.routes.authenticate
import com.devalpesh.service.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
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
                BasicApiResponse<Unit>(
                    success = false, message = ApiResponseMessages.USER_ALREADY_EXIST
                )
            )
            return@post
        }

        when (service.validateCreateAccountRequest(request)) {
            is UserService.ValidationEvents.ErrorFieldEmpty -> {
                call.respond(
                    BasicApiResponse<Unit>(
                        success = false, message = ApiResponseMessages.FIELDS_BLANK
                    )
                )
            }

            is UserService.ValidationEvents.SuccessEvent -> {
                service.createUser(request)
                call.respond(
                    BasicApiResponse<Unit>(success = true)
                )
            }

            else -> {
                return@post
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

        val user = userService.getUserByEmail(request.email) ?: kotlin.run {
            call.respond(
                HttpStatusCode.OK, BasicApiResponse<Unit>(
                    success = false, message = ApiResponseMessages.INVALID_CREDENTIALS
                )
            )
            return@post
        }

        val isCorrectPassword = userService.isValidPassword(
            enteredPassword = request.password,
            actualPassword = user.password
        )
        if (isCorrectPassword) {

            val expiresIn = 1000L * 60L * 60L * 24L * 365L

            val token = JWT.create()
                .withClaim("userId", user.id)
                .withIssuer(jwtIssuer)
                .withExpiresAt(Date(System.currentTimeMillis() + expiresIn))
                .withAudience(jwtAudience)
                .sign(Algorithm.HMAC256(jwtSecret))
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse(
                    success = true,
                    data = AuthResponse(
                        userId = user.id,
                        token = token
                    )
                )
            )
        } else {
            call.respond(
                HttpStatusCode.OK, BasicApiResponse<Unit>(
                    success = false, message = ApiResponseMessages.INVALID_CREDENTIALS
                )
            )
        }
    }
}

fun Route.authenticate() {
    authenticate {
        get("/api/user/authenticate") {
            call.respond(HttpStatusCode.OK)
        }
    }
}