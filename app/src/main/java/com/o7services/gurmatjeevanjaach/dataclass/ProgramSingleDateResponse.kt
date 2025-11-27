package com.o7services.gurmatjeevanjaach.dataclass

import com.google.gson.annotations.SerializedName

data class ProgramSingleDateResponse(
    @SerializedName("success" ) var success : Boolean? = null,
    @SerializedName("status"  ) var status  : Int?     = null,
    @SerializedName("message" ) var message : String?  = null,
    @SerializedName("data"    ) var data    : ArrayList<ProgramSingleDateResponse.Data> = arrayListOf()
){
    data class Data (
        @SerializedName("id"             ) var id             : Int?    = null,
        @SerializedName("autoId"         ) var autoId         : Int?    = null,
        @SerializedName("title"          ) var title          : String? = null,
        @SerializedName("address"        ) var address        : String? = null,
        @SerializedName("details"        ) var details        : String? = null,
        @SerializedName("imageUrl"       ) var imageUrl       : String? = null,
        @SerializedName("mapLink"        ) var mapLink        : String? = null,
        @SerializedName("startDate"      ) var startDate      : String? = null,
        @SerializedName("endDate"        ) var endDate        : String? = null,
        @SerializedName("contactNumber1" ) var contactNumber1 : Double?    = null,
        @SerializedName("contactNumber2" ) var contactNumber2 : Double?    = null,
        @SerializedName("isDeleted"      ) var isDeleted      : Int?    = null,
        @SerializedName("isBlocked"      ) var isBlocked      : Int?    = null,
        @SerializedName("addedById"      ) var addedById      : Int?    = null,
        @SerializedName("updatedById"    ) var updatedById    : Int?    = null,
        @SerializedName("status"         ) var status         : Int?    = null,
        @SerializedName("created_at"     ) var createdAt      : String? = null,
        @SerializedName("updated_at"     ) var updatedAt      : String? = null

    )
}
