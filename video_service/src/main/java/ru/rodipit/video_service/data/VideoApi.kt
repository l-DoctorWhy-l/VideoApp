package ru.rodipit.video_service.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url
import ru.rodipit.video_service.data.dto.SmallVideoDto
import ru.rodipit.video_service.data.dto.VideosResponseDto

internal interface VideoApi {

    @GET("videos")
    suspend fun loadVideos(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 10,
        @Query("query") query: String = "doctor who", // В api нет возможности получить просто ленту видео, поэтому беру по отдельному запросу
        @Query("fields") fields: String = "name,link,duration,pictures",
    ): Response<VideosResponseDto>

    @GET
    suspend fun loadNextPage(
        @Url source: String,
    ): Response<VideosResponseDto>

    @GET("videos/{video_id}")
    suspend fun loadVideo(
        @Path("video_id") videoId: String,
        @Query("fields") fields: String = "name,link",
    ): Response<SmallVideoDto>

}