package com.example.ecotec.models

data class ResponseReportes(
    val status: Boolean,
    val pendientes: Int,
    val atendidos: Int,
    val total: Int
)
