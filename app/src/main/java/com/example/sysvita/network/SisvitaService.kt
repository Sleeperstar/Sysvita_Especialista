package com.example.sysvita.network

import com.example.sysvita.data.ApiResponse
import com.example.sysvita.data.CuestionarioCompletoResponse
import com.example.sysvita.data.RegisterRequest
import com.example.sysvita.data.CuestionariosResponse
import com.example.sysvita.data.LoginRequest
import com.example.sysvita.data.RegisterEspecialistaRequest
import com.example.sysvita.data.RespuestasResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SisvitaService {

    @POST("/registrar")
    suspend fun registerUser(@Body user: RegisterRequest): Response<Unit>

    @POST("/iniciarSesion")
    suspend fun loginUser(@Body user: LoginRequest): Response<ApiResponse>

    @GET("cuestionarios")
    fun getCuestionarios(): Call<CuestionariosResponse>

    @POST("cuestionarioCompleto")
    fun getCuestionarioCompleto(@Body idCuest: Map<String, Int>): Call<CuestionarioCompletoResponse>

    @POST("obtenerRespuestas")
    fun getRespuestas(@Body idCuest: Map<String, Int>): Call<RespuestasResponse>

    @POST("/registrarEspecialista")
    suspend fun registerEspecialista(@Body especialista: RegisterEspecialistaRequest): Response<Unit>
}










