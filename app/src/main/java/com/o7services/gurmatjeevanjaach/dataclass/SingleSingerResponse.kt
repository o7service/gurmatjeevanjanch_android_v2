package com.o7services.gurmatjeevanjaach.dataclass

import com.google.gson.annotations.SerializedName

data class SingleSingerResponse(

    @SerializedName("success" ) var success : Boolean? = null,
    @SerializedName("status"  ) var status  : Int?     = null,
    @SerializedName("message" ) var message : String?  = null,
    @SerializedName("data"    ) var data    : Data?    = Data()

){

    data class Data (

        @SerializedName("id"          ) var id          : Int?    = null,
        @SerializedName("autoId"      ) var autoId      : Int?    = null,
        @SerializedName("name"        ) var name        : String? = null,
        @SerializedName("imageUrl"    ) var imageUrl    : String? = null,
        @SerializedName("isDeleted"   ) var isDeleted   : Int?    = null,
        @SerializedName("isBlocked"   ) var isBlocked   : Int?    = null,
        @SerializedName("addedById"   ) var addedById   : Int?    = null,
        @SerializedName("updatedById" ) var updatedById : Int?    = null,
        @SerializedName("status"      ) var status      : Int?    = null,
        @SerializedName("created_at"  ) var createdAt   : String? = null,
        @SerializedName("updated_at"  ) var updatedAt   : String? = null

    )
}
