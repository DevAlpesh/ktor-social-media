package com.devalpesh.routes

import com.devalpesh.data.request.CreateCommentRequest
import com.devalpesh.data.request.DeleteCommentRequest
import com.devalpesh.data.response.ApiResponseMessages
import com.devalpesh.data.response.BasicApiResponse
import com.devalpesh.service.CommentService
import com.devalpesh.service.LikeService
import com.devalpesh.service.UserService
import com.devalpesh.util.QueryParams
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.createComments(
    commentService: CommentService,
    userService: UserService,
) {
    authenticate {
        post("/api/comment/create") {
            val request = call.receiveNullable<CreateCommentRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            ifEmailBelongsToUser(
                userId = request.userId,
                validateEmail = userService::doesEmailBelongToUserId
            ) {
                when (commentService.createComment(request)) {
                    is CommentService.ValidationEvent.ErrorFieldEmpty -> {
                        call.respond(
                            HttpStatusCode.OK,
                            BasicApiResponse(
                                success = false,
                                ApiResponseMessages.FIELDS_BLANK
                            )
                        )
                    }

                    is CommentService.ValidationEvent.CommentTooLong -> {
                        call.respond(
                            HttpStatusCode.OK,
                            BasicApiResponse(
                                success = false,
                                ApiResponseMessages.COMMENT_TOO_LONG
                            )
                        )
                    }

                    is CommentService.ValidationEvent.Success -> {
                        call.respond(
                            HttpStatusCode.OK,
                            BasicApiResponse(
                                success = true
                            )
                        )
                    }
                }
            }

        }
    }
}

fun Route.getCommentsForPost(
    commentService: CommentService
) {
    authenticate {
        get("/api/comment/get") {
            val postId = call.parameters[QueryParams.PARAM_POST_ID] ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val comments = commentService.getCommentsForPost(postId)

            call.respond(
                HttpStatusCode.OK,
                comments
            )
        }
    }
}

fun Route.deleteComments(
    commentService: CommentService,
    userService: UserService,
    likeService: LikeService
) {
    authenticate {
        delete("/api/comment/delete") {
            val request = call.receiveNullable<DeleteCommentRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }

            ifEmailBelongsToUser(
                userId = request.userId,
                validateEmail = userService::doesEmailBelongToUserId
            ) {
                val deleted = commentService.deleteComment(request.commentId)
                if (deleted) {
                    likeService.deleteLikesForParent(request.commentId)
                    call.respond(HttpStatusCode.OK, BasicApiResponse(success = true))
                } else {
                    call.respond(HttpStatusCode.NotFound, BasicApiResponse(success = false))
                }
            }
        }
    }
}