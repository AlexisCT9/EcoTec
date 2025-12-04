package com.example.ecotec.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // ⭐ BASE URL GLOBAL USANDO NGROK (HTTPS)
    private const val BASE_URL = "https://countersalient-perla-illusorily.ngrok-free.dev/ecotec_api/"

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
