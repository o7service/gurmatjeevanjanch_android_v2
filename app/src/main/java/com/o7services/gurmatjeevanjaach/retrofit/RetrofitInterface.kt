package com.o7services.gurmatjeevanjaach.retrofit

import com.o7services.gurmatjeevanjaach.dataclass.AddSamagamRequest
import com.o7services.gurmatjeevanjaach.dataclass.AddSamagamResponse
import com.o7services.gurmatjeevanjaach.dataclass.AllLinkRequest
import com.o7services.gurmatjeevanjaach.dataclass.AllLinkResponse
import com.o7services.gurmatjeevanjaach.dataclass.AllProgramResponse
import com.o7services.gurmatjeevanjaach.dataclass.AllSingerResponse
import com.o7services.gurmatjeevanjaach.dataclass.HomeResponse
import com.o7services.gurmatjeevanjaach.dataclass.ProgramSingleDateRequest
import com.o7services.gurmatjeevanjaach.dataclass.ProgramSingleDateResponse
import com.o7services.gurmatjeevanjaach.dataclass.SingleSingerAudioRequest
import com.o7services.gurmatjeevanjaach.dataclass.SingleSingerAudioResponse
import com.o7services.gurmatjeevanjaach.dataclass.SingleSingerRequest
import com.o7services.gurmatjeevanjaach.dataclass.SingleSingerResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RetrofitInterface {
    @POST("link/category")
    fun getLinkCategory(@Body request : AllLinkRequest) : Call<AllLinkResponse>
    @POST("singers/all")
    @FormUrlEncoded
    fun getAllSinger(
        @Field("startpoint") startPoint: Int,
        @Field("limit") limit: Int
    ): Call<AllSingerResponse>

    @POST("singers/single")
    fun getSingleSinger(@Body request : SingleSingerRequest) : Call<SingleSingerResponse>
    @POST("singer/audio")
    fun getSingleSingerAudio(@Body request : SingleSingerAudioRequest) : Call<SingleSingerAudioResponse>
    @POST("programs/all")
    @FormUrlEncoded
    fun getAllPrograms(
        @Field("startpoint") startPoint: Int,
        @Field("limit") limit: Int
    ): Call<AllProgramResponse>
    @POST("programs/date")
    fun getSingleProgramByDate(@Body request: ProgramSingleDateRequest): Call<ProgramSingleDateResponse>
    @POST("samagam/add")
    fun addSamagam(@Body request : AddSamagamRequest): Call<AddSamagamResponse>
    @POST("home")
    fun home() : Call<HomeResponse>
}