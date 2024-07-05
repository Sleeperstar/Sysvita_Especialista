package com.example.sysvita.viewModel

import com.example.sysvita.data.ApiResponse
import com.example.sysvita.data.User
import com.example.sysvita.data.LoginRequest
import com.example.sysvita.data.LoginUser
import com.example.sysvita.data.RegisterEspecialistaRequest
import com.example.sysvita.data.RegisterRequest
import com.example.sysvita.network.RetrofitInstance


class UserRepository {

    private val sisvitaService = RetrofitInstance.api

    suspend fun registerUser(user: User): Boolean {
        return try {
            val request = RegisterRequest(
                nombre = user.nombre,
                apellidos = user.apellidos,
                num_cel = user.num_cel,
                edad = user.edad,
                sexo = user.sexo,
                id_ubi = user.id_ubi,
                email = user.email,
                contra = user.contra,
                id_tipo_usu = user.id_tipo_usu
            )
            val response = sisvitaService.registerUser(request)
            response.isSuccessful
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun registerEspecialista(especialista: RegisterEspecialistaRequest): Boolean {
        return try {
            val response = sisvitaService.registerEspecialista(especialista)
            response.isSuccessful
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun loginUser(loginUser: LoginUser): ApiResponse {
        return try {
            val request = LoginRequest(
                email = loginUser.email,
                contra = loginUser.password
            )
            val response = sisvitaService.loginUser(request)
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                ApiResponse(null, "Error en el inicio de sesión", false)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ApiResponse(null, "Error en la solicitud: ${e.message}", false)
        }
    }
}


