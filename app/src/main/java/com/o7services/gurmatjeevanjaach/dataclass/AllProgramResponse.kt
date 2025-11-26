package com.o7services.gurmatjeevanjaach.dataclass

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class AllProgramResponse(
    @SerializedName("success"       ) var success       : Boolean? = null,
    @SerializedName("status"        ) var status        : Int?     = null,
    @SerializedName("message"       ) var message       : String?  = null,
    @SerializedName("distinctDates" ) var distinctDates : Int?     = null,
    @SerializedName("data"          ) var data  : Map<String , List<SamagamItem>>
){
    @Parcelize
    data class SamagamItem(
        val id: Int,
        val autoId: Int,
        val title: String,
        val address: String,
        val details: String,
        val imageUrl: String?,
        val mapLink: String?,
        val startDate: String,
        val endDate: String,
        val contactNumber1: Long?,
        val contactNumber2: Long?,
        val isDeleted: Int,
        val isBlocked: Int,
        val addedById: Int?,
        val updatedById: Int?,
        val status: Int,
        val created_at: String,
        val updated_at: String
    ): Parcelable


}
