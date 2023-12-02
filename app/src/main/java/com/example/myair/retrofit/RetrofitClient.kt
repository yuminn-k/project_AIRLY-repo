package com.example.myair.retrofit

import com.example.myair.Values
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    companion object {
        private val gson = GsonBuilder()
            .setLenient()
            .create()

        private val client: Retrofit = Retrofit.Builder()
            .baseUrl(Values.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val airsApi: AirsApi = client.create(AirsApi::class.java)
    }
}