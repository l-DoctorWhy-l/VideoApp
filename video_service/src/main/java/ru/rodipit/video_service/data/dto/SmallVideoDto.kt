package ru.rodipit.video_service.data.dto

import com.google.gson.annotations.SerializedName
import ru.rodipit.models.SmallVideoModel

internal data class SmallVideoDto(
    @SerializedName("name") val name: String,
    @SerializedName("link") val source: String,
)

internal fun SmallVideoDto.toModel(): SmallVideoModel {
    return SmallVideoModel(
        id = this.source.split("/").last(),
        title = this.name,
        sourceLink = STUB_SOURCE_VIDEO_URL,
    )
}

// API не отдаёт прямую ссылку на видео, поэтому заменяю её на стабовую
// В целом не нашёл другого хорошего API в интернете
private const val STUB_SOURCE_VIDEO_URL = "https://videocdn.cdnpk.net/joy/content/video/free/2014-12/large_preview/Raindrops_Videvo.mp4?token=exp=1740224658~acl=/*~hmac=07ae3b9f35879d55b8e42e880a0e49a12ef934adf8baa18427cd6621d412fa5e"