package com.devalpesh.di

import com.devalpesh.repository.user.FakeUserRepository
import org.koin.dsl.module

internal val testModule = module {
    single {
        FakeUserRepository()
    }
}