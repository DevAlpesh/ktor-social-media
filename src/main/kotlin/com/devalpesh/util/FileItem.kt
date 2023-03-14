package com.devalpesh.util

import io.ktor.http.content.*
import java.io.File
import java.util.*

fun PartData.FileItem.save(path: String): String {
    val fileByte = this.streamProvider().readBytes()
    val fileExtension = this.originalFileName?.takeLastWhile { it != '.' }
    val fileName = UUID.randomUUID().toString() + "." + fileExtension
    val folder = File("src/main/${path}")
    folder.mkdir()
    File("src/main/${path}$fileName").writeBytes(fileByte)
    return fileName
}










