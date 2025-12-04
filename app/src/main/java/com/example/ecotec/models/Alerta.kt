package com.example.ecotec.models

data class Alerta(
    val notif_id: String,
    val bote_id: String,
    val tipo_notificacion: String,
    val mensaje: String,
    val nivel: Int,
    val tipo_residuo: String?,
    val fecha: String,
    val leido: Int
)

data class ResponseAlertas(
    val success: Boolean,
    val alertas: List<Alerta>
)
