package com.o7services.gurmatjeevanjaach.dataclass

import com.google.gson.annotations.SerializedName

data class AddSamagamResponse(
    @SerializedName("success" ) var success : Boolean? = null,
    @SerializedName("message" ) var message : String?  = null,
    @SerializedName("data"    ) var data    : Data?    = Data()
){
    data class Data (

        @SerializedName("autoId"        ) var autoId        : Int?    = null,
        @SerializedName("organizerName" ) var organizerName : String? = null,
        @SerializedName("address"       ) var address       : String? = null,
        @SerializedName("details"       ) var details       : String? = null,
        @SerializedName("mapLink"       ) var mapLink       : String? = null,
        @SerializedName("phone"         ) var phone         : String? = null,
        @SerializedName("email"         ) var email         : String? = null,
        @SerializedName("startDate"     ) var startDate     : String? = null,
        @SerializedName("endDate"       ) var endDate       : String? = null,
        @SerializedName("updated_at"    ) var updatedAt     : String? = null,
        @SerializedName("created_at"    ) var createdAt     : String? = null,
        @SerializedName("id"            ) var id            : Int?    = null

    )
}
