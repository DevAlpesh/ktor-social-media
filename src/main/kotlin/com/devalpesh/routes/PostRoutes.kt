package com.devalpesh.routes

import com.devalpesh.data.request.CreatePostRequest
import com.devalpesh.data.request.DeletePostRequest
import com.devalpesh.data.response.ApiResponseMessages
import com.devalpesh.data.response.BasicApiResponse
import com.devalpesh.service.CommentService
import com.devalpesh.service.LikeService
import com.devalpesh.service.PostService
import com.devalpesh.util.Constant
import com.devalpesh.util.QueryParams
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.createPost(
    postService: PostService
) {
    authenticate {
        post("/api/post/create") {
            val request = call.receiveNullable<CreatePostRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            val didUserExist = postService.createPostIfUserExists(request, call.userId)
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

fun Route.getPostForFollows(
    postService: PostService
) {
    authenticate {
        get("/api/post/get") {


            val page = call.parameters[QueryParams.PARAM_PAGE]?.toIntOrNull() ?: 0
            val pageSize = call.parameters[QueryParams.PARAM_PAGE_SIZE]?.toIntOrNull()
                ?: Constant.DEFAULT_POST_PAGE_SIZE

            val post = postService.getPostForFollows(
                call.userId,
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

fun Route.deletePost(
    postService: PostService,
    likeService: LikeService,
    commentService: CommentService
) {
    authenticate {
        delete("/api/post/delete") {
            val request = call.receiveNullable<DeletePostRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }

            val post = postService.getPost(request.postId)

            if (post == null) {
                call.respond(
                    HttpStatusCode.NotFound,
                )
                return@delete
            }
            if (post.userId == call.userId) {
                postService.deletePost(request.postId)
                likeService.deleteLikesForParent(request.postId)
                commentService.deleteCommentsForPost(request.postId)
                call.respond(
                    HttpStatusCode.OK
                )
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }
    }
}






















