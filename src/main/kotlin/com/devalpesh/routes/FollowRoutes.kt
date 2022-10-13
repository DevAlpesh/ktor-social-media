package com.devalpesh.routes

import com.devalpesh.data.request.FollowUpdateRequest
import com.devalpesh.data.response.ApiResponseMessages.USER_NOT_FOUND
import com.devalpesh.data.response.BasicApiResponse
import com.devalpesh.service.FollowService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.followRoute(
    followService: FollowService
) {
    authenticate {
        post("/api/following/follow") {
            val request = call.receiveNullable<FollowUpdateRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            if (followService.followUserIfExists(request,call.userId)) {
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

}

fun Route.unfollowUser(followService: FollowService) {
    authenticate {
        delete("/api/following/unfollow") {
            val request = call.receiveNullable<FollowUpdateRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }
            if (followService.unFollowUserIfExists(request,call.userId)) {
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
}
