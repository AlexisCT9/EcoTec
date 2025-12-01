package com.example.ecotec.api

import com.example.ecotec.models.*
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("dashboard/get_total_usuarios.php")
    fun getTotalUsuarios(): Call<ResponseTotal>

    @GET("dashboard/get_total_basureros.php")
    fun getTotalBasureros(): Call<ResponseTotal>

    @GET("dashboard/get_estadisticas_reportes.php")
    fun getEstadisticasReportes(): Call<ResponseReportes>

    @GET("dashboard/get_basureros_estado.php")
    fun getBasurerosEstado(): Call<ResponseBasureros>
}
