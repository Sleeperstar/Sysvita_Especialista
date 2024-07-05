package com.example.sysvita.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.sysvita.data.RegisterEspecialistaRequest
import com.example.sysvita.navigation.Screen
import com.example.sysvita.viewModel.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterEspecialistaScreen(navController: NavHostController) {
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var hospital by remember { mutableStateOf("") }
    var especialidad by remember { mutableStateOf("") }
    var consultorio by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val userRepository = UserRepository()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Registro Especialista",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = apellido,
                onValueChange = { apellido = it },
                label = { Text("Apellido") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = hospital,
                onValueChange = { hospital = it },
                label = { Text("Hospital") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = especialidad,
                onValueChange = { especialidad = it },
                label = { Text("Especialidad") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = consultorio,
                onValueChange = { consultorio = it },
                label = { Text("Consultorio") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        Icon(
                            painter = painterResource(
                                id = if (passwordVisibility) com.example.sysvita.R.drawable.visibility else com.example.sysvita.R.drawable.visibility_off
                            ),
                            contentDescription = null
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            Button(
                onClick = {
                    when {
                        nombre.isBlank() -> {
                            Toast.makeText(context, "Debe ingresar el nombre.", Toast.LENGTH_SHORT).show()
                        }
                        apellido.isBlank() -> {
                            Toast.makeText(context, "Debe ingresar el apellido.", Toast.LENGTH_SHORT).show()
                        }
                        email.isBlank() -> {
                            Toast.makeText(context, "Debe ingresar el email.", Toast.LENGTH_SHORT).show()
                        }
                        hospital.isBlank() -> {
                            Toast.makeText(context, "Debe ingresar el hospital.", Toast.LENGTH_SHORT).show()
                        }
                        especialidad.isBlank() -> {
                            Toast.makeText(context, "Debe ingresar la especialidad.", Toast.LENGTH_SHORT).show()
                        }
                        consultorio.isBlank() -> {
                            Toast.makeText(context, "Debe ingresar el consultorio.", Toast.LENGTH_SHORT).show()
                        }
                        password.isBlank() -> {
                            Toast.makeText(context, "Debe ingresar una contraseña.", Toast.LENGTH_SHORT).show()
                        }
                        !validatePassword(password) -> {
                            Toast.makeText(context, "La contraseña debe ser mayor o igual a 8 caracteres.", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            val especialista = RegisterEspecialistaRequest(
                                nombre = nombre,
                                apellido = apellido,
                                correo = email,
                                password = password,
                                hospital = hospital,
                                especialidad = especialidad,
                                consultorio = consultorio
                            )

                            coroutineScope.launch {
                                val success = userRepository.registerEspecialista(especialista)
                                withContext(Dispatchers.Main) {
                                    if (success) {
                                        Toast.makeText(context, "Registro completado con éxito...", Toast.LENGTH_SHORT).show()

                                        navController.navigate(Screen.Login.route)
                                    } else {
                                        Toast.makeText(context, "Error en el registro. Inténtelo de nuevo.", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Registrarse")
            }
        }
    }
}
