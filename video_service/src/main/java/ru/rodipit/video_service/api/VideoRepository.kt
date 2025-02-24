package ru.rodipit.video_service.api

import ru.rodipit.models.SmallVideoModel
import ru.rodipit.models.VideoModel
import ru.rodipit.utils.network.MyResult


interface VideoRepository {

    suspend fun loadVideos(source: String?): MyResult<VideosPagingResult>

    suspend fun loadVideo(id: String): MyResult<SmallVideoModel>

}

data class VideosPagingResult(
    val nextPage: String?,
    val videos: List<VideoModel>
)