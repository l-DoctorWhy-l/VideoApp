package ru.rodipit.video_service.data.dto

import com.google.gson.annotations.SerializedName

internal data class VideosResponseDto(
    @SerializedName("paging") val paging: PagingDto,
    @SerializedName("data") val data: List<VideoDto>,
)

internal data class PagingDto(
    @SerializedName("next") val next: String?,
)