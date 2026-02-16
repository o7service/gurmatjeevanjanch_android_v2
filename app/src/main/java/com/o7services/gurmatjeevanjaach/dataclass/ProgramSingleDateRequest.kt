package com.o7services.gurmatjeevanjaach.dataclass

import com.google.gson.annotations.SerializedName

data class ProgramSingleDateRequest(
    var date : String ?= "",
    val startpoint: Int,
    val limit: Int
)
