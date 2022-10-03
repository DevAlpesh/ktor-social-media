package com.devalpesh.routes

import com.devalpesh.data.models.Post
import com.devalpesh.data.repository.post.PostRepository
import com.devalpesh.data.request.CreatePostRequest
import com.devalpesh.data.response.ApiResponseMessages
import com.devalpesh.data.response.BasicApiResponse
import com.devalpesh.service.PostService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.createPostRoute(postService: PostService) {
    post("/api/post/create") {
        val request = call.receiveNullable<CreatePostRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        val didUserExist = postService.createPostIfUserExists(request)
        if (!didUserExist) {
            call.respond(
                HttpStatusCode.OK, BasicApiResponse(
                    success = false,
                    message = ApiResponseMessages.USER_NOT_FOUND
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