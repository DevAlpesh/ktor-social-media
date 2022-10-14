package com.devalpesh.routes

import com.devalpesh.service.ActivityService
import com.devalpesh.util.Constant
import com.devalpesh.util.QueryParams
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getActivities(
    activityService: ActivityService
) {
    authenticate {
        get("/api/activity/get") {
            val page = call.parameters[QueryParams.PARAM_PAGE]?.toIntOrNull() ?: 0
            val pageSize = call.parameters[QueryParams.PARAM_PAGE_SIZE]?.toIntOrNull()
                ?: Constant.DEFAULT_POST_PAGE_SIZE

            val activities = activityService.getActivitiesForUser(
                call.userId,
                page,
                pageSize
            )
            call.respond(
                HttpStatusCode.OK,
                activities
            )
        }
    }
}
