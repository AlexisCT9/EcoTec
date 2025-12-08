package com.example.ecotec.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {


    //private const val BASE_URL = "http://192.168.1.81/ecotec_api/"
    private const val BASE_URL = "http://10.247.163.12/ecotec_api/"

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
