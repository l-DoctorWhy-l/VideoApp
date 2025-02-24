package ru.rodipit.database.api.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import ru.rodipit.database.api.dbo.VideoDbo

@Dao
abstract class VideoDao {

    @Insert
    abstract suspend fun insert(videos: List<VideoDbo>)

    @Query("DELETE FROM video_database")
    abstract suspend fun deleteAllVideos()

    @Query("SELECT * FROM video_database")
    abstract suspend fun getAllVideos(): List<VideoDbo>

    @Transaction
    open suspend fun deleteAndInsertTransaction(videos: List<VideoDbo>) {
        deleteAllVideos()
        insert(videos)
    }

}