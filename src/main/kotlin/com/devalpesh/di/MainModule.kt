package com.devalpesh.di

import com.devalpesh.data.repository.comment.CommentRepository
import com.devalpesh.data.repository.comment.CommentRepositoryImpl
import com.devalpesh.data.repository.follow.FollowRepository
import com.devalpesh.data.repository.follow.FollowRepositoryImpl
import com.devalpesh.data.repository.likes.LikesRepository
import com.devalpesh.data.repository.likes.LikesRepositoryImpl
import com.devalpesh.data.repository.post.PostRepository
import com.devalpesh.data.repository.post.PostRepositoryImpl
import com.devalpesh.data.repository.user.UserRepository
import com.devalpesh.data.repository.user.UserRepositoryImpl
import com.devalpesh.service.*
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

    single<LikesRepository> {
        LikesRepositoryImpl(get())
    }

    single<CommentRepository> {
        CommentRepositoryImpl(get())
    }

    single { FollowService(get()) }
    single { UserService(get()) }
    single { PostService(get()) }
    single { LikeService(get()) }
    single { CommentService(get()) }
}