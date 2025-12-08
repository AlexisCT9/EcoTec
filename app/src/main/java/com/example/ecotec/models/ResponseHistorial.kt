package com.example.ecotec.models

data class HistorialItem(
    val lectura_id: String?,       // id real del backend
    val bote_id: String?,
    val estado: String?,           // antes lo llamabas "tipo"
    val nivel: Int?,               // antes "nivel_previo"
    val tipo_residuo: String?,
    val ubicacion: String?,
    val fecha: String?
)

data class ResponseHistorial(
    val success: Boolean,
    val historial: List<HistorialItem>
)
