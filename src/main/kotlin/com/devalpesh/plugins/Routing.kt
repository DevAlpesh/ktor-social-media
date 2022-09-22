package com.devalpesh.plugins

import com.devalpesh.data.repository.follow.FollowRepository
import com.devalpesh.data.repository.user.UserRepository
import com.devalpesh.routes.createUserRoute
import com.devalpesh.routes.followRoute
import com.devalpesh.routes.loginUser
import com.devalpesh.routes.unfollowUser
import io.ktor.server.routing.*
import io.ktor.server.application.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val userRepository: UserRepository by inject()
    val followRepository: FollowRepository by inject()
    routing {
        // User routes
        createUserRoute(userRepository)
        loginUser(userRepository)

        // Following routes
        followRoute(followRepository)
        unfollowUser(followRepository)
    }
}
