package com.devalpesh.plugins

import com.devalpesh.data.repository.follow.FollowRepository
import com.devalpesh.data.repository.user.UserRepository
import com.devalpesh.routes.*
import com.devalpesh.service.FollowService
import com.devalpesh.service.PostService
import com.devalpesh.service.UserService
import io.ktor.server.routing.*
import io.ktor.server.application.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {

    val userService: UserService by inject()
    val followService: FollowService by inject()
    val postService: PostService by inject()

    val jwtIssuer = environment.config.property("jwt.domain").getString()
    val jwtAudience = environment.config.property("jwt.audience").getString()
    val jwtSecret = environment.config.property("jwt.secret").getString()


    routing {
        // User routes
        createUserRoute(userService)
        loginUser(
            userService = userService,
            jwtIssuer = jwtIssuer,
            jwtAudience = jwtAudience,
            jwtSecret = jwtSecret
        )

        // Following routes
        followRoute(followService)
        unfollowUser(followService)

        // Post routes
        createPostRoute(
            postService = postService,
            userService = userService
        )
    }
}
