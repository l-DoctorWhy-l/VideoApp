package ru.rodipit.design.components.model

data class VideoItemUiData(
    val name: String,
    val thumbnail: String,
    val duration: String,
    val id: String
) {

    companion object {
        fun forPreview() = VideoItemUiData(
            name = "VideoasdasdadasdasdadadadadadadadadadadVideoasdasdadasdasdadadadadadadadadadadVideoasdasdadasdasdadadadadadadadadadadVideoasdasdadasdasdadadadadadadadadadad",
            thumbnail = "",
            duration = "12:20:12",
            id = "12"
        )
    }

}
