package com.example.sysvita.data

data class ApiResponse(
    val data: UserData?,
    val message: String,
    val success: Boolean
)
data class UserData(
    val edad: Int,
    val id_per: Int,
    val id_usu: Int,
    val nom_completo: String,
    val num_cel: Int,
    val sexo: String,
    val tipo_usuario: String
)