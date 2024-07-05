package com.example.sysvita.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.sysvita.data.ApiResponse
import com.example.sysvita.data.CuestionarioCompletoData
import com.example.sysvita.data.CuestionariosResponse
import com.example.sysvita.data.CuestionarioCompletoResponse
import com.example.sysvita.data.Cuestionario
import com.example.sysvita.data.Respuesta
import com.example.sysvita.data.RespuestasResponse
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.sysvita.data.Pregunta
import com.example.sysvita.network.RetrofitInstance

@Composable
fun CuestionarioCompletoScreen(navController: NavHostController, cuestionarioId: Int) {
    // Variables de estado para almacenar las preguntas, título, descripción y mensajes de error
    var preguntas by remember { mutableStateOf(listOf<Pregunta>()) }
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val respuestasSeleccionadas = remember { mutableStateOf(mutableMapOf<Int, String>()) }

    // Efecto que se lanza cuando el composable es iniciado o el cuestionarioId cambia
    LaunchedEffect(cuestionarioId) {
        try {
            // Realizar una llamada de red en un hilo de IO para obtener el cuestionario completo
            val response = withContext(Dispatchers.IO) {
                RetrofitInstance.api.getCuestionarioCompleto(mapOf("id_cuest" to cuestionarioId)).execute()
            }
            if (response.isSuccessful) {
                // Si la respuesta es exitosa, actualizar las variables de estado con los datos obtenidos
                response.body()?.data?.let { data ->
                    titulo = data.cuestionario.titulo
                    descripcion = data.cuestionario.descripcion
                    preguntas = data.preguntas
                }
            } else {
                // Si la respuesta no es exitosa, mostrar el mensaje de error
                errorMessage = "Error: ${response.message()}"
            }
        } catch (e: Exception) {
            // Manejar excepciones y mostrar el mensaje de error
            errorMessage = "Exception: ${e.message}"
        }
    }

    // Surface es un contenedor que establece el fondo y el padding del contenido
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        // Column organiza los elementos verticalmente
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize()
        ) {
            if (errorMessage.isNotEmpty()) {
                // Mostrar el mensaje de error si existe
                Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
            } else {
                // Mostrar el título y la descripción del cuestionario
                Text(
                    text = titulo,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = descripcion,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                // LazyColumn para mostrar una lista perezosa de preguntas
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(preguntas) { pregunta ->
                        PreguntaCard(pregunta, cuestionarioId, respuestasSeleccionadas)
                    }
                }
                // Botón para enviar los resultados del cuestionario
                Button(
                    onClick = {
                        // Calcular la puntuación total y navegar a la pantalla de resultados
                        val totalPuntuacion = calcularPuntuacion(cuestionarioId, respuestasSeleccionadas.value)
                        navController.navigate("resultados/$cuestionarioId/$totalPuntuacion")
                    },
                    modifier = Modifier.padding(vertical = 16.dp)
                ) {
                    Text(text = "Enviar Resultados")
                }
            }
        }
    }
}

@Composable
fun PreguntaCard(pregunta: Pregunta, cuestionarioId: Int, respuestasSeleccionadas: MutableState<MutableMap<Int, String>>) {
    // Determinar las opciones de respuesta según el cuestionarioId
    val respuestas = when (cuestionarioId) {
        100 -> listOf("Ninguna o poca parte del tiempo", "A veces", "Buena parte del tiempo", "La mayor parte o todo el tiempo")
        101 -> listOf("Nada", "Un poco", "Moderadamente", "Mucho")
        102 -> listOf("Casi nunca", "A veces", "A menudo", "Casi siempre")
        103 -> listOf("Nada", "Levemente, no me molestó mucho", "Moderadamente, fue muy desagradable pero pude soportarlo", "Severadamente, apenas pude soportarlo")
        else -> listOf()
    }

    // Variable de estado para la opción seleccionada
    var selectedOption by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
    ) {
        // Tarjeta que muestra la pregunta
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = pregunta.pregunta,
                    fontSize = 18.sp,
                    color = Color.White
                )
            }
        }

        // Mostrar las opciones de respuesta como filas con radio buttons
        respuestas.forEach { respuesta ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable {
                        selectedOption = respuesta
                        respuestasSeleccionadas.value[pregunta.id_preg] = respuesta
                    }
            ) {
                RadioButton(
                    selected = selectedOption == respuesta,
                    onClick = {
                        selectedOption = respuesta
                        respuestasSeleccionadas.value[pregunta.id_preg] = respuesta
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = respuesta)
            }
        }
    }
}

@Composable
fun ResultadosScreen(navController: NavHostController, cuestionarioId: Int, totalPuntuacion: Int) {
    // Determinar el resultado según el cuestionarioId y la puntuación total
    val resultado = when (cuestionarioId) {
        100 -> when {
            totalPuntuacion in 20..44 -> "No tiene ansiedad"
            totalPuntuacion in 45..59 -> "Tiene algunos rasgos de ansiedad"
            totalPuntuacion in 60..80 -> "Tiene ansiedad"
            else -> "Puntuación fuera de rango"
        }
        101, 102 -> when {
            totalPuntuacion in 20..37 -> "No tiene ansiedad"
            totalPuntuacion in 38..44 -> "Tiene algunos rasgos de ansiedad"
            totalPuntuacion in 45..80 -> "Tiene ansiedad"
            else -> "Puntuación fuera de rango"
        }
        103 -> when {
            totalPuntuacion in 0..7 -> "No tiene ansiedad"
            totalPuntuacion in 8..15 -> "Tiene algunos rasgos de ansiedad"
            totalPuntuacion in 16..63 -> "Tiene ansiedad"
            else -> "Puntuación fuera de rango"
        }
        else -> "Cuestionario no reconocido"
    }

    // Surface es un contenedor que establece el fondo y el padding del contenido
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        // Column organiza los elementos verticalmente
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            // Mostrar los resultados del cuestionario
            Text(
                text = "Resultados del Cuestionario",
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "Puntuación Total: $totalPuntuacion",
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = resultado,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Botón para regresar a la pantalla anterior
            Button(
                onClick = { navController.popBackStack() }
            ) {
                Text(text = "Regresar")
            }
        }
    }
}
