package com.devalpesh.plugins

import com.devalpesh.repository.user.UserRepository
import com.devalpesh.routes.createUserRoute
import io.ktor.server.routing.*
import io.ktor.server.application.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val userRepository: UserRepository by inject()
    routing {
        createUserRoute(userRepository)
    }
}
