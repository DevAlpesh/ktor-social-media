package com.devalpesh.routes

import com.devalpesh.data.models.Post
import com.devalpesh.data.repository.post.PostRepository
import com.devalpesh.data.request.CreatePostRequest
import com.devalpesh.data.response.ApiResponseMessages
import com.devalpesh.data.response.BasicApiResponse
import com.devalpesh.plugins.email
import com.devalpesh.service.PostService
import com.devalpesh.service.UserService
import com.devalpesh.util.Constant
import com.devalpesh.util.QueryParams
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.createPostRoute(
    postService: PostService, userService: UserService
) {
    authenticate {
        post("/api/post/create") {
            val request = call.receiveNullable<CreatePostRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            ifEmailBelongsToUser(
                userId = request.userId,
                validateEmail = userService::doesEmailBelongToUserId
            ) {
                val didUserExist = postService.createPostIfUserExists(request)
                if (!didUserExist) {
                    call.respond(
                        HttpStatusCode.OK, BasicApiResponse(
                            success = false, message = ApiResponseMessages.USER_NOT_FOUND
                        )
                    )
                } else {
                    call.respond(
                        HttpStatusCode.OK, BasicApiResponse(
                            success = true
                        )
                    )
                }
            }
        }
    }
}

fun Route.getPostForFollows(
    postService: PostService, userService: UserService
) {
    authenticate {
        get() {
            val userId = call.parameters[QueryParams.PARAM_USER_ID] ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val page = call.parameters[QueryParams.PARAM_PAGE]?.toIntOrNull() ?: 0
            val pageSize = call.parameters[QueryParams.PARAM_PAGE_SIZE]?.toIntOrNull()
                ?: Constant.DEFAULT_POST_PAGE_SIZE

            ifEmailBelongsToUser(
                userId = userId,
                validateEmail = userService::doesEmailBelongToUserId
            ) {
                val post = postService.getPostForFollows(
                    userId,
                    page,
                    pageSize
                )
                call.respond(
                    HttpStatusCode.OK,
                    post
                )
            }
        }
    }
}






















