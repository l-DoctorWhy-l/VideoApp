package ru.rodipit.database.api

import ru.rodipit.database.api.dao.VideoDao
import ru.rodipit.database.db.VideoRoomDatabase

class VideoDatabase internal constructor(
    private val roomDatabase: VideoRoomDatabase,
) {
    val videoDao: VideoDao
        get() = roomDatabase.videoDao()

}