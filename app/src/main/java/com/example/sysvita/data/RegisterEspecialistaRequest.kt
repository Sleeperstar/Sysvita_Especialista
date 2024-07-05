package com.example.sysvita.data

data class RegisterEspecialistaRequest(
    val nombre: String,
    val apellido: String,
    val correo: String,
    val password: String,
    val hospital: String,
    val especialidad: String,
    val consultorio: String
)