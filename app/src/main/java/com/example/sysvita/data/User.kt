package com.example.sysvita.data

data class User(
    val nombre: String,
    val apellidos: String,
    val num_cel: String,
    val edad: Int,
    val sexo: String,
    val id_ubi: Int = 761,
    val email: String,
    val contra: String,
    val id_tipo_usu: Int
)

