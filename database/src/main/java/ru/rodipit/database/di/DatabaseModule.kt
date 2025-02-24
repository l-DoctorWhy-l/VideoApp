package ru.rodipit.database.di

import org.koin.dsl.module
import ru.rodipit.database.api.VideoDatabase
import ru.rodipit.database.db.VideoRoomDatabase

val databaseModule = module {

    single {
        VideoDatabase(
            roomDatabase = VideoRoomDatabase.create(
                context = get(),
            )
        )
    }
    factory {
        get<VideoDatabase>().videoDao
    }
}