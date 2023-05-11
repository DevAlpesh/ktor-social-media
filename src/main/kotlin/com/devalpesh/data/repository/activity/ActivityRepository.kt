package com.devalpesh.data.repository.activity

import com.devalpesh.data.models.Activity
import com.devalpesh.data.response.ActivityResponse
import com.devalpesh.util.Constant

interface ActivityRepository {

    suspend fun getActivitiesForUser(
        userId: String,
        page: Int = 0,
        pageSize: Int = Constant.DEFAULT_ACTIVITY_PAGE_SIZE
    ): List<ActivityResponse>


    suspend fun createActivity(activity: Activity)

    suspend fun deleteActivity(activityId: String): Boolean

}