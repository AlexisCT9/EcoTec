package com.example.ecotec.models

data class Basurero(
    val bote_id: String,
    val ubicacion: String,
    val nivel: Int,
    val estado: String,
    val tipo_residuo: String,
    val sensor_ultrasonico: String,
    val deteccion_camara: String,
    val servo_estado: String,
    val bocina_estado: String,
    val fecha_actualizacion: String
)
