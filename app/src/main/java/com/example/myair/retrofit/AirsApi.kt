package com.example.myair.retrofit

import com.example.myair.model.AirQualityResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AirsApi {
    @GET("B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty")
    fun getAir(
        @Query("sidoName") sidoName: String,
        @Query("pageNo") pageNo: Int,
        @Query("numOfRows") numOfRows: Int,
        @Query("returnType") returnType: String,
        @Query("serviceKey") serviceKey: String,
        @Query("ver") ver: String
    ): Call<AirQualityResponse>
}
