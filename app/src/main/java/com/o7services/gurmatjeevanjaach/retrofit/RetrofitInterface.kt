package com.o7services.gurmatjeevanjaach.retrofit

import com.o7services.gurmatjeevanjaach.dataclass.AddSamagamRequest
import com.o7services.gurmatjeevanjaach.dataclass.AddSamagamResponse
import com.o7services.gurmatjeevanjaach.dataclass.AllLinkRequest
import com.o7services.gurmatjeevanjaach.dataclass.AllLinkResponse
import com.o7services.gurmatjeevanjaach.dataclass.AllProgramResponse
import com.o7services.gurmatjeevanjaach.dataclass.AllSingerResponse
import com.o7services.gurmatjeevanjaach.dataclass.ProgramSingleDateRequest
import com.o7services.gurmatjeevanjaach.dataclass.ProgramSingleDateResponse
import com.o7services.gurmatjeevanjaach.dataclass.SingleSingerAudioRequest
import com.o7services.gurmatjeevanjaach.dataclass.SingleSingerAudioResponse
import com.o7services.gurmatjeevanjaach.dataclass.SingleSingerRequest
import com.o7services.gurmatjeevanjaach.dataclass.SingleSingerResponse
import com.o7services.gurmatjeevanjaach.dataclass.SingleYoutubeDataClass
import com.o7services.gurmatjeevanjaach.dataclass.SocialLinkResponse
import com.o7services.gurmatjeevanjaach.dataclass.SocialLinkSingleRequest
import com.o7services.gurmatjeevanjaach.dataclass.SocialLinkSingleResponse
import com.o7services.gurmatjeevanjaach.dataclass.YoutubeResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitInterface {
    @POST("youtube/links/all")
    fun getAllYoutube(): Call<YoutubeResponse>
    @POST("youtube/link/single")
    fun getSingleYoutube(@Body request : SingleYoutubeDataClass) : Call<YoutubeResponse>
    @POST("category/all")
    fun getAllCategory(): Call<SocialLinkResponse>
    @POST("category/single")
    fun getSingleCategory(@Body request : SocialLinkSingleRequest) : Call<SocialLinkSingleResponse>
    @POST("link/category")
    fun getLinkCategory(@Body request : AllLinkRequest) : Call<AllLinkResponse>
    @POST("singers/all")
    fun getAllSinger() : Call<AllSingerResponse>
    @POST("singers/single")
    fun getSingleSinger(@Body request : SingleSingerRequest) : Call<SingleSingerResponse>
    @POST("singer/audio")
    fun getSingleSingerAudio(@Body request : SingleSingerAudioRequest) : Call<SingleSingerAudioResponse>
    @POST("programs/all")
    fun getAllPrograms(): Call<AllProgramResponse>
    @POST("programs/date")
    fun getSingleProgramByDate(@Body request: ProgramSingleDateRequest): Call<ProgramSingleDateResponse>
    @POST("samagam/add")
    fun addSamagam(@Body request : AddSamagamRequest): Call<AddSamagamResponse>
}