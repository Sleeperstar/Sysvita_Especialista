package com.example.sysvita.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Login : Screen("login")
    object Register : Screen("register/{idTipoUsuario}") {
        fun createRoute(idTipoUsuario: Int) = "register/$idTipoUsuario"
    }
    object RegisterEspecialista : Screen("registerEspecialista")
    object Cuestionarios : Screen("cuestionarios")
    object CuestionarioCompleto : Screen("cuestionarioCompleto")
    object Resultados : Screen("resultados")
    object SeleccionTipoRegistro : Screen("seleccionTipoRegistro")
}
