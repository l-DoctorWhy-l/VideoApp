package ru.rodipit.database.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.rodipit.database.api.dbo.VideoDbo
import ru.rodipit.database.api.dao.VideoDao

@Database(entities = [VideoDbo::class], version = 1, exportSchema = false)
internal abstract class VideoRoomDatabase : RoomDatabase() {

    abstract fun videoDao(): VideoDao

    companion object {

        fun create(context: Context): VideoRoomDatabase {
            return Room.databaseBuilder(
                context = context.applicationContext,
                klass = VideoRoomDatabase::class.java,
                name = "video_database",
            ).build()
        }

        const val VIDEO_DATABASE_NAME = "video_database"
    }
}