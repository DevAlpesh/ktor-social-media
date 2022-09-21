package com.devalpesh

import com.devalpesh.di.mainModule
import io.ktor.server.application.*
import com.devalpesh.plugins.*
import org.koin.core.context.startKoin
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    install(Koin) {
        modules(mainModule)
    }
    configureSerialization()
    configureMonitoring()
    configureHTTP()
    configureSecurity()
    configureRouting()
}
