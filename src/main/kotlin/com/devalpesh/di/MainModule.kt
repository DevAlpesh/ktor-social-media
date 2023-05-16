package com.devalpesh.di

import com.devalpesh.data.models.Skill
import com.devalpesh.data.repository.activity.ActivityRepository
import com.devalpesh.data.repository.activity.ActivityRepositoryImpl
import com.devalpesh.data.repository.comment.CommentRepository
import com.devalpesh.data.repository.comment.CommentRepositoryImpl
import com.devalpesh.data.repository.follow.FollowRepository
import com.devalpesh.data.repository.follow.FollowRepositoryImpl
import com.devalpesh.data.repository.likes.LikesRepository
import com.devalpesh.data.repository.likes.LikesRepositoryImpl
import com.devalpesh.data.repository.post.PostRepository
import com.devalpesh.data.repository.post.PostRepositoryImpl
import com.devalpesh.data.repository.skill.SkillRepository
import com.devalpesh.data.repository.skill.SkillRepositoryImpl
import com.devalpesh.data.repository.user.UserRepository
import com.devalpesh.data.repository.user.UserRepositoryImpl
import com.devalpesh.service.*
import com.devalpesh.util.Constant
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val mainModule = module {
    single {
        val client = KMongo.createClient().coroutine
        client.getDatabase(Constant.DATABASE_NAME)/*.also {
            val skill = it.getCollection<Skill>()
            GlobalScope.launch {
                skill.insertOne(Skill(name = "Adobe XD", imageUrl = "http://10.0.2.2:8001/skills/adobe-logo.svg"))
                skill.insertOne(Skill(name = "Android", imageUrl = "http://10.0.2.2:8001/skills/android-logo.svg"))
                skill.insertOne(Skill(name = "C#", imageUrl = "http://10.0.2.2:8001/skills/csharp-logo.svg"))
                skill.insertOne(Skill(name = "JavaScript", imageUrl = "http://10.0.2.2:8001/skills/java-script-logo.svg"))
                skill.insertOne(Skill(name = "MongoDB", imageUrl = "http://10.0.2.2:8001/skills/mongodb-logo.svg"))
            }
        }*/
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

    single<ActivityRepository> {
        ActivityRepositoryImpl(get())
    }
    single<SkillRepository> {
        SkillRepositoryImpl(get())
    }

    single { FollowService(get()) }
    single { UserService(get(), get()) }
    single { PostService(get()) }
    single { LikeService(get(), get(), get()) }
    single { CommentService(get(), get()) }
    single { ActivityService(get(), get(), get()) }
    single { SkillService(get()) }

    single { Gson() }
}













