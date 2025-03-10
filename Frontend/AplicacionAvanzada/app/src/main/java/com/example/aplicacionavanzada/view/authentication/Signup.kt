package com.example.aplicacionavanzada.view.authentication

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aplicacionavanzada.R
import com.example.aplicacionavanzada.view.dialogs.ToastMessage
import com.example.aplicacionavanzada.viewmodel.authentication.AuthState
import com.example.aplicacionavanzada.viewmodel.authentication.AuthViewModel

@Composable
fun Signup(authViewModel: AuthViewModel, navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Estado de autenticación
    val authState = authViewModel.authState.collectAsState()
    val currentState = authState.value

    LaunchedEffect(currentState) {
        when (currentState) {

            // Mensaje de bienvenida
            is AuthState.Authenticated -> {

                Toast.makeText(
                    context,
                    context.getString(R.string.welcome_message),
                    Toast.LENGTH_SHORT
                ).show()

                navController.navigate("principal") {
                    popUpTo("login") {
                        inclusive = true
                    }
                }
            }

            // Mensaje de error
            is AuthState.Error -> {
                val messageId = ToastMessage.getStringResourceId(currentState.messageKey)
                val translatedMessage = context.getString(messageId)

                Toast.makeText(
                    context,
                    translatedMessage,
                    Toast.LENGTH_LONG
                ).show()
            }

            else -> Unit
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título
        Text(
            text = stringResource(id = R.string.signup_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Imagen
        Image(
            painter = painterResource(id = R.drawable.signup),
            contentDescription = stringResource(id = R.string.signup_image_desc),
            modifier = Modifier
                .height(325.dp)
                .padding(bottom = 16.dp)
        )

        // Campo de texto para el email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(stringResource(id = R.string.email_label)) },
            shape = MaterialTheme.shapes.extraLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de texto para la contraseña
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(id = R.string.password_label)) },
            shape = MaterialTheme.shapes.extraLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            visualTransformation = PasswordVisualTransformation() // Ocultar la contraseña
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de crear cuenta
        Button(
            onClick = { authViewModel.signUp(email, password) },
            enabled = authState.value != AuthState.Loading,
            modifier = Modifier.padding(horizontal = 24.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Text(stringResource(id = R.string.signup_button))
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Botón de login
        TextButton(
            onClick = { navController.navigate("login") },
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                stringResource(id = R.string.login_prompt),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}