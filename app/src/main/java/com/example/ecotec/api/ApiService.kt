package com.example.ecotec.api

import com.example.ecotec.models.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    // ============================
    // DASHBOARD
    // ============================
    @GET("get_total_usuarios.php")
    fun getTotalUsuarios(): Call<ResponseTotal>

    @GET("get_total_basureros.php")
    fun getTotalBasureros(): Call<ResponseTotal>

    @GET("get_estadisticas_reportes.php")
    fun getEstadisticasReportes(): Call<ResponseReportes>

    @GET("get_basureros_estado.php")
    fun getBasurerosEstado(): Call<ResponseBasureros>


    // ============================
    // BASUREROS
    // ============================
    @GET("basureros/get_basureros.php")
    fun getBasureros(): Call<ResponseBasureros>

    @GET("basureros/get_basurero_detalle.php")
    fun getBasureroDetalle(
        @Query("bote_id") id: String
    ): Call<ResponseDetalleBasurero>

    @FormUrlEncoded
    @POST("basureros/marcar_atendido.php")
    fun marcarAtendido(
        @Field("id") id: String
    ): Call<ResponseSimple>


    // ============================
    // HISTORIAL LIMPIEZA
    // ============================
    @GET("clean/get_historial.php")
    fun getHistorialLimpieza(): Call<ResponseHistorial>


    // ============================
    // ALERTAS
    // ============================
    @GET("alertas/get_alertas_pendientes.php")
    fun getAlertasPendientes(): Call<ResponseAlertas>

    @GET("alertas/resolver_alerta.php")
    fun resolverAlerta(
        @Query("notif_id") notifId: String
    ): Call<ResponseSimple>
    @GET("usuarios/get_usuarios.php")
    fun getUsuarios(): Call<ResponseUsuarios>


}
