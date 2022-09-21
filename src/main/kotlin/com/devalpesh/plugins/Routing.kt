package com.devalpesh.plugins

import com.devalpesh.routes.userRoutes
import io.ktor.server.routing.*
import io.ktor.server.application.*

fun Application.configureRouting() {
    routing {
        userRoutes()
    }
}
