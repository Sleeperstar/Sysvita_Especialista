package com.example.sysvita.screen

fun calcularPuntuacion(cuestionarioId: Int, respuestasSeleccionadas: Map<Int, String>): Int {
    val valorRespuestas = when (cuestionarioId) {
        100 -> mapOf(
            "Ninguna o poca parte del tiempo" to 1,
            "A veces" to 2,
            "Buena parte del tiempo" to 3,
            "La mayor parte o todo el tiempo" to 4
        )
        101 -> mapOf(
            "Nada" to 1,
            "Un poco" to 2,
            "Moderadamente" to 3,
            "Mucho" to 4
        )
        102 -> mapOf(
            "Casi nunca" to 1,
            "A veces" to 2,
            "A menudo" to 3,
            "Casi siempre" to 4
        )
        103 -> mapOf(
            "Nada" to 1,
            "Levemente, no me molestó mucho" to 2,
            "Moderadamente, fue muy desagradable pero pude soportarlo" to 3,
            "Severamente, apenas pude soportarlo" to 4
        )
        else -> emptyMap()
    }

    return respuestasSeleccionadas.values.sumOf { valorRespuestas[it] ?: 0 }
}
