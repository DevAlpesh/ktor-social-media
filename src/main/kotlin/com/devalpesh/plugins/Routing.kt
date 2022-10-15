package com.devalpesh.plugins

import com.devalpesh.routes.*
import com.devalpesh.service.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {

    val userService: UserService by inject()
    val followService: FollowService by inject()
    val postService: PostService by inject()
    val likeService: LikeService by inject()
    val commentService: CommentService by inject()
    val activityService: ActivityService by inject()

    val jwtIssuer = environment.config.property("jwt.domain").getString()
    val jwtAudience = environment.config.property("jwt.audience").getString()
    val jwtSecret = environment.config.property("jwt.secret").getString()


    routing {
        // User routes
        createUser(userService)
        loginUser(
            userService = userService,
            jwtIssuer = jwtIssuer,
            jwtAudience = jwtAudience,
            jwtSecret = jwtSecret
        )
        searchUser(userService)
        getUserProfile(userService)
        getPostForProfile(postService)

        // following routes
        followRoute(followService, activityService)
        unfollowUser(followService)

        // post routes
        createPost(postService)
        getPostForFollows(postService)
        deletePost(postService, likeService, commentService)

        // like routes
        likeParent(likeService, activityService)
        unlikeParent(likeService, activityService)

        //comments routes
        createComments(commentService, activityService)
        deleteComments(commentService, likeService)
        getCommentsForPost(commentService)

        // activities routes
        getActivities(activityService)

    }
}
