package com.devalpesh.routes

import com.devalpesh.data.response.ApiResponseMessages
import com.devalpesh.data.response.BasicApiResponse
import com.devalpesh.data.request.UpdateProfileRequest
import com.devalpesh.data.response.UserResponseItem
import com.devalpesh.service.PostService
import com.devalpesh.service.UserService
import com.devalpesh.util.Constant
import com.devalpesh.util.Constant.BANNER_DIR
import com.devalpesh.util.Constant.BANNER_IMAGE_PATH
import com.devalpesh.util.Constant.BASE_URL
import com.devalpesh.util.Constant.PROFILE_DIR
import com.devalpesh.util.Constant.PROFILE_PICTURE_PATH
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


fun Route.searchUser(
    userService: UserService
) {
    authenticate {
        get("/api/user/search") {
            val query = call.parameters[QueryParams.PARAM_QUERY]

            if (query.isNullOrBlank()) {
                call.respond(
                    HttpStatusCode.OK, listOf<UserResponseItem>()
                )
                return@get
            }
            val searchResult = userService.searchForUser(query, call.userId)

            call.respond(
                HttpStatusCode.OK, searchResult
            )
        }
    }
}

fun Route.getUserProfile(
    userService: UserService
) {
    authenticate {
        get("/api/user/profile") {
            val userId = call.parameters[QueryParams.PARAM_USER_ID]

            if (userId.isNullOrBlank()) {
                call.respond(
                    HttpStatusCode.BadRequest,
                )
                return@get
            }
            val profileResponse = userService.getUserProfile(userId, call.userId)
            if (profileResponse == null) {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse<Unit>(
                        success = false,
                        message = ApiResponseMessages.USER_NOT_FOUND
                    )
                )
                return@get
            }
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse(
                    success = true,
                    data = profileResponse
                )
            )
        }
    }
}

fun Route.updateUserProfile(
    userService: UserService
) {
    val gson: Gson by inject()
    authenticate {
        put("/api/user/update") {
            val multipart = call.receiveMultipart()
            var updateProfileRequest: UpdateProfileRequest? = null
            var profilePictureFileName: String? = null
            var bannerImageFileName: String? = null
            multipart.forEachPart { partData ->
                when (partData) {
                    is PartData.FormItem -> {
                        if (partData.name == "update_profile_data") {
                            updateProfileRequest = gson.fromJson(
                                partData.value, UpdateProfileRequest::class.java
                            )
                        }
                    }

                    is PartData.FileItem -> {
                        if (partData.name == "profile_picture") {
                            profilePictureFileName = partData.save(PROFILE_PICTURE_PATH)
                        } else if (partData.name == "banner_image") {
                            bannerImageFileName = partData.save(BANNER_IMAGE_PATH)
                        }
                    }

                    is PartData.BinaryItem -> Unit
                    else -> Unit
                }
            }

            val profilePictureUrl = "$BASE_URL$PROFILE_DIR/$profilePictureFileName"
            val bannerImageUrl = "$BASE_URL$BANNER_DIR/$bannerImageFileName"

            updateProfileRequest?.let { request ->
                val updateAcknowledged = userService.updateUser(
                    userId = call.userId,
                    bannerImageUrl = if (bannerImageFileName == null)
                        null
                    else {
                        bannerImageUrl
                    },
                    profileImageUrl = if (profilePictureFileName == null) {
                        null
                    } else {
                        profilePictureUrl
                    },
                    updateProfileRequest = request
                )
                if (updateAcknowledged) {
                    call.respond(
                        HttpStatusCode.OK, BasicApiResponse<Unit>(
                            success = true
                        )
                    )
                    return@put
                } else {
                    File("$PROFILE_PICTURE_PATH/$profilePictureFileName").delete()
                    call.respond(HttpStatusCode.InternalServerError)
                    return@put
                }
            } ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@put
            }
        }
    }
}




















