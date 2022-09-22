package com.devalpesh.routes

import com.devalpesh.data.repository.follow.FollowRepository
import com.devalpesh.data.request.FollowUpdateRequest
import com.devalpesh.data.response.ApiResponseMessages.USER_NOT_FOUND
import com.devalpesh.data.response.BasicApiResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.followRoute(
    followRepository: FollowRepository
) {
    post("/api/following/follow") {
        val request = call.receiveNullable<FollowUpdateRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val didUserExists = followRepository.followUserIfExists(
            request.followingUserId,
            request.followedUserId
        )

        if (didUserExists) {
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse(success = true)
            )
        } else {
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse(
                    success = false,
                    message = USER_NOT_FOUND
                )
            )
        }
    }
}

fun Route.unfollowUser(followRepository: FollowRepository) {
    delete("/api/following/unfollow") {

        val request = call.receiveNullable<FollowUpdateRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }

        val didUserExists = followRepository.unfollowedUserIfExists(
            request.followingUserId,
            request.followedUserId
        )

        if (didUserExists) {
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse(success = true)
            )
        } else {
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse(
                    success = false,
                    message = USER_NOT_FOUND
                )
            )
        }
    }
}
