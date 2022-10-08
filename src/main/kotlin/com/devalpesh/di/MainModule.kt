package com.devalpesh.di

import com.devalpesh.data.repository.follow.FollowRepository
import com.devalpesh.data.repository.follow.FollowRepositoryImpl
import com.devalpesh.data.repository.post.PostRepository
import com.devalpesh.data.repository.post.PostRepositoryImpl
import com.devalpesh.data.repository.user.UserRepository
import com.devalpesh.data.repository.user.UserRepositoryImpl
import com.devalpesh.data.request.CreatePostRequest
import com.devalpesh.service.FollowService
import com.devalpesh.service.PostService
import com.devalpesh.service.UserService
import com.devalpesh.util.Constant
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val mainModule = module {
    single {
        val client = KMongo.createClient().coroutine
        client.getDatabase(Constant.DATABASE_NAME)
    }

    single<UserRepository> {
        UserRepositoryImpl(get())
    }

    single<FollowRepository> {
        FollowRepositoryImpl(get())
    }

    single<PostRepository> {
        PostRepositoryImpl(get())
    }

    single { FollowService(get()) }

    single { UserService(get()) }

    single { PostService(get()) }
}