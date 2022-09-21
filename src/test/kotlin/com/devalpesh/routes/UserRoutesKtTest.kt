package com.devalpesh.routes

import com.devalpesh.data.models.User
import com.devalpesh.data.request.CreateAccountRequest
import com.devalpesh.data.response.ApiResponseMessages
import com.devalpesh.data.response.BasicApiResponse
import com.devalpesh.di.testModule
import com.devalpesh.plugins.configureSerialization
import com.devalpesh.repository.user.FakeUserRepository
import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.BeforeTest


internal class UserRoutesKtTest : KoinTest {

    private val userRepository by inject<FakeUserRepository>()

    private val gson = Gson()

    @BeforeTest
    fun setUp() {
        startKoin {
            modules(testModule)
        }
    }

    @Test
    fun `Create user, no body attached, responds with UnsupportedMediaType`() {
        withTestApplication(
            moduleFunction = {
                install(Routing) {
                    createUserRoute(userRepository)
                }
            }
        ) {
            val request = handleRequest(
                method = HttpMethod.Post,
                uri = "/api/user/create"
            ) {
                addHeader("Content-type", "application/json")
            }
            assertThat(request.response.status()).isEqualTo(HttpStatusCode.UnsupportedMediaType)
        }
    }

    @Test
    fun `Create user, user already exist, responds with unsuccessful`() = runBlocking {
        val user = User(
            email = "test@gmail.com",
            username = "test",
            password = "test",
            profileImageUrl = "",
            bio = "",
            githubUrl = null,
            linkedInUrl = null,
            instagramUrl = null
        )
        userRepository.createUser(user)

        withTestApplication(
            moduleFunction = {
                configureSerialization()
                install(Routing) {
                    createUserRoute(userRepository)
                }
            }
        ) {
            val request = handleRequest(
                method = HttpMethod.Post,
                uri = "/api/user/create"
            ) {
                addHeader("Content-type", "application/json")
                val request = CreateAccountRequest(
                    email = "test@gmail.com",
                    username = "",
                    password = ""
                )
                setBody(gson.toJson(request))
            }

            val response = gson.fromJson(
                request.response.content ?: "",
                BasicApiResponse::class.java
            )

            assertThat(response.success).isFalse()
            assertThat(response.message).isEqualTo(ApiResponseMessages.USER_ALREADY_EXIST)
        }
    }

    @Test
    fun `Create user, email is empty, responds with unsuccessful`() = runBlocking {
        val user = User(
            email = "test@gmail.com",
            username = "test",
            password = "test",
            profileImageUrl = "",
            bio = "",
            githubUrl = null,
            linkedInUrl = null,
            instagramUrl = null
        )
        userRepository.createUser(user)

        withTestApplication(
            moduleFunction = {
                configureSerialization()
                install(Routing) {
                    createUserRoute(userRepository)
                }
            }
        ) {
            val request = handleRequest(
                method = HttpMethod.Post,
                uri = "/api/user/create"
            ) {
                addHeader("Content-type", "application/json")
                val request = CreateAccountRequest(
                    email = "",
                    username = "",
                    password = ""
                )
                setBody(gson.toJson(request))
            }

            val response = gson.fromJson(
                request.response.content ?: "",
                BasicApiResponse::class.java
            )

            assertThat(response.success).isFalse()
            assertThat(response.message).isEqualTo(ApiResponseMessages.FIELDS_BLANK)
        }
    }

    @Test
    fun `Create user, valid, responds with success`() {
        withTestApplication(
            moduleFunction = {
                configureSerialization()
                install(Routing) {
                    createUserRoute(userRepository)
                }
            }
        ) {
            val request = handleRequest(
                method = HttpMethod.Post,
                uri = "/api/user/create"
            ) {
                addHeader("Content-type", "application/json")
                val request = CreateAccountRequest(
                    email = "test@test.com",
                    username = "test",
                    password = "test"
                )
                setBody(gson.toJson(request))
            }

            val response = gson.fromJson(
                request.response.content ?: "",
                BasicApiResponse::class.java
            )

            assertThat(response.success).isTrue()

            runBlocking {
                val isUserInDb = userRepository.getUserByEmail("test@test.com") != null
                assertThat(isUserInDb).isTrue()
            }
        }
    }
}