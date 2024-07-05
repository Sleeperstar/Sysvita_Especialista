package com.example.sysvita.data

data class CuestionariosResponse(
    val data: List<Cuestionario>,
    val message: String,
    val success: Boolean
)