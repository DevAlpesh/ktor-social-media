package com.devalpesh

import com.devalpesh.di.mainModule
import com.devalpesh.plugins.*
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin
import java.nio.file.Paths

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
















