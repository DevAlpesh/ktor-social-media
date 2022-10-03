package com.devalpesh.plugins

import com.devalpesh.data.repository.follow.FollowRepository
import com.devalpesh.data.repository.user.UserRepository
import com.devalpesh.routes.createUserRoute
import com.devalpesh.routes.followRoute
import com.devalpesh.routes.loginUser
import com.devalpesh.routes.unfollowUser
import com.devalpesh.service.FollowService
import com.devalpesh.service.UserService
import io.ktor.server.routing.*
import io.ktor.server.application.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val userService: UserService by inject()
    val followService: FollowService by inject()
    routing {
        // User routes
        createUserRoute(userService)
        loginUser(userService)

        // Following routes
        followRoute(followService)
        unfollowUser(followService)
    }
}
