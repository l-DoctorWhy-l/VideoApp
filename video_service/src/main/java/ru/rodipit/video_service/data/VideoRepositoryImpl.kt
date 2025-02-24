package ru.rodipit.video_service.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import ru.rodipit.database.api.dao.VideoDao
import ru.rodipit.database.api.dbo.VideoDbo
import ru.rodipit.models.VideoModel
import ru.rodipit.utils.network.MyResult
import ru.rodipit.video_service.api.VideoRepository
import ru.rodipit.video_service.api.VideosPagingResult
import ru.rodipit.video_service.data.dto.toModel
import java.io.IOException

internal class VideoRepositoryImpl(
    private val api: VideoApi,
    private val dao: VideoDao,
) : VideoRepository {

    override suspend fun loadVideos(source: String?): MyResult<VideosPagingResult> =
        withContext(Dispatchers.IO) {
            val cachedVideos = async { dao.getAllVideos() }
            return@withContext try {
                val response = if (source == null) {
                    api.loadVideos()
                } else {
                    api.loadNextPage(source = source)
                }
                if (response.isSuccessful) {
                    val videos = response.body()?.data?.map { it.toModel() }
                    if (videos?.isNotEmpty() == true) {
                        dao.deleteAndInsertTransaction(videos.map { it.toDbo() })
                    }
                    MyResult.Success(
                        VideosPagingResult(
                            videos = videos ?: emptyList(),
                            nextPage = response.body()?.paging?.next,
                        )
                    )
                } else {
                    if (cachedVideos.await().isNotEmpty() && source == null) {
                        MyResult.ErrorWithContent(
                            data = VideosPagingResult(
                                videos = cachedVideos.await().map { it.toModel() },
                                nextPage = null,
                            ),
                            exception = IOException("Failed to fetch data: ${response.code()}")
                        )
                    } else {
                        MyResult.Error(IOException("Failed to fetch data: ${response.code()}"))
                    }
                }
            } catch (e: Exception) {
                if (cachedVideos.await().isNotEmpty() && source == null) {
                    MyResult.ErrorWithContent(
                        data = VideosPagingResult(
                            videos = cachedVideos.await().map { it.toModel() },
                            nextPage = null,
                        ),
                        exception = e,
                    )
                } else {
                    MyResult.Error(e)
                }
            }
        }

    override suspend fun loadVideo(id: String) = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = api.loadVideo(videoId = id)
            if (response.isSuccessful) {
                response.body()?.let {
                    MyResult.Success(it.toModel())
                } ?: MyResult.Error(IOException("Empty body"))
            } else {
                MyResult.Error(IOException("Failed to fetch data: ${response.code()}"))
            }
        } catch (e: Exception) {
            MyResult.Error(e)
        }
    }

}

internal fun VideoDbo.toModel(): VideoModel {
    return VideoModel(
        id = this.id,
        title = this.title,
        duration = this.duration,
        sourceLink = this.sourceLink,
        thumbnail = this.thumbnail,
    )
}

internal fun VideoModel.toDbo(): VideoDbo {
    return VideoDbo(
        id = this.id,
        title = this.title,
        duration = this.duration,
        sourceLink = this.sourceLink,
        thumbnail = this.thumbnail,
    )
}