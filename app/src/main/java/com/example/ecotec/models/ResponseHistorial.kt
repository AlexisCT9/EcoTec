package com.example.ecotec.models

data class HistorialItem(
    val id: String,
    val bote_id: String,
    val ubicacion: String,
    val tipo: String,
    val nivel_previo: Int,
    val fecha: String
)

data class ResponseHistorial(
    val success: Boolean,
    val historial: List<HistorialItem>
)
