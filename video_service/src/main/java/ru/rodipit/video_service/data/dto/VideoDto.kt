package ru.rodipit.video_service.data.dto

import com.google.gson.annotations.SerializedName
import ru.rodipit.models.VideoModel

internal data class VideoDto(
    @SerializedName("name") val name: String,
    @SerializedName("duration") val duration: Long,
    @SerializedName("pictures") val picture: PictureDto,
    @SerializedName("link") val source: String,
)

internal data class PictureDto(
    @SerializedName("base_link") val link: String,
)

internal fun VideoDto.toModel(): VideoModel {
    return VideoModel(
        id = this.source.split("/").last(),
        title = this.name,
        duration = this.duration,
        sourceLink = this.source,
        thumbnail = this.picture.link,
    )
}
