package com.devalpesh.di

import com.devalpesh.controller.user.UserController
import com.devalpesh.controller.user.UserControllerImpl
import com.devalpesh.util.Constant
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val mainModule = module {
    single {
        val client = KMongo.createClient().coroutine
        client.getDatabase(Constant.DATABASE_NAME)
    }

    single<UserController> {
        UserControllerImpl(get())
    }
}