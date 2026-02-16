package com.o7services.gurmatjeevanjaach.dataclass

data class SingleSingerAudioRequest(
    var singerId : String ?= "",
        val startpoint: Int,
        val limit: Int
    )

