package com.example.sysvita.data

data class RespuestasResponse(
    val data: List<Respuesta>,
    val message: String,
    val success: Boolean
)