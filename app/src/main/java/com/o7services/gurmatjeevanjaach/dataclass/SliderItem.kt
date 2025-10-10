package com.o7services.gurmatjeevanjaach.dataclass

sealed class SliderItem {
    data class YouTubeVideo(val videoId: String) : SliderItem()
    data class Image(val imageUrl: String) : SliderItem()
}
