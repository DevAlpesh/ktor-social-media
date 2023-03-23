package com.devalpesh.routes

import com.devalpesh.data.request.CreatePostRequest
import com.devalpesh.data.request.DeletePostRequest
import com.devalpesh.data.response.ApiResponseMessages
import com.devalpesh.data.response.BasicApiResponse
import com.devalpesh.data.response.UpdateProfileRequest
import com.devalpesh.service.CommentService
import com.devalpesh.service.LikeService
import com.devalpesh.service.PostService
import com.devalpesh.service.UserService
import com.devalpesh.util.Constant
import com.devalpesh.util.QueryParams
import com.devalpesh.util.save
import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.io.File
import java.util.*

fun Route.createPost(
    postService: PostService,
) {
    val gson by inject<Gson>()
    authenticate {
        post("/api/post/create") {
            val multipart = call.receiveMultipart()
            var createPostRequest: CreatePostRequest? = null
            var fileName: String? = null
            multipart.forEachPart { partData ->
                when (partData) {
                    is PartData.FormItem -> {
                        if (partData.name == "post_data") {
                            createPostRequest = gson.fromJson(
                                partData.value,
                                CreatePostRequest::class.java
                            )
                        }
                    }

                    is PartData.FileItem -> {
                        fileName = partData.save(Constant.POST_PICTURE_PATH)
                    }

                    is PartData.BinaryItem -> Unit
                    else -> Unit
                }
            }

            val postPictureUrl = "${Constant.BASE_URL}src/main/${Constant.POST_PICTURE_PATH}$fileName"

            createPostRequest?.let { request ->
                val createPostAcknowledged = postService.createPost(
                    request = request,
                    userId = call.userId,
                    imageUrl = postPictureUrl
                )
                if (createPostAcknowledged) {
                    call.respond(
                        HttpStatusCode.OK, BasicApiResponse<Unit>(
                            success = true
                        )
                    )
                } else {
                    File("${Constant.POST_PICTURE_PATH}/$fileName").delete()
                    call.respond(HttpStatusCode.InternalServerError)
                }
            } ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
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























