package com.o7services.gurmatjeevanjaach.dataclass

import com.google.gson.annotations.SerializedName

sealed class SliderItem {
    data class YouTubeVideo(val videoId: String) : SliderItem()
    data class CustomImageWithId(val imageResId: Int, val link: String) : SliderItem()
}
