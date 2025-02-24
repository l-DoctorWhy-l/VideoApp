package ru.rodipit.database.api.dbo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.rodipit.database.db.VideoRoomDatabase

@Entity(tableName = VideoRoomDatabase.VIDEO_DATABASE_NAME)
data class VideoDbo(
    @PrimaryKey @ColumnInfo("id") val id: String,
    @ColumnInfo("title") val title: String,
    @ColumnInfo("thumbnail") val thumbnail: String,
    @ColumnInfo("sourceLink") val sourceLink: String,
    @ColumnInfo("duration") val duration: Long,
)