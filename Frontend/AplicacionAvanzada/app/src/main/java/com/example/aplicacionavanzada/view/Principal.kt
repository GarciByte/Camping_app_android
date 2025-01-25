package com.example.aplicacionavanzada.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aplicacionavanzada.R
import com.example.aplicacionavanzada.model.AppScreens
import com.example.aplicacionavanzada.viewmodel.authentication.AuthState
import com.example.aplicacionavanzada.viewmodel.authentication.AuthViewModel

@Composable
fun Principal(navController: NavController, authViewModel: AuthViewModel) {

    // Estado de autenticación
    val authState by authViewModel.authState.observeAsState(AuthState.Unauthenticated)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // Título
            Text(
                text = stringResource(R.string.home),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineMedium,
            )

            // Logo de la aplicación
            Image(
                painter = painterResource(id = R.drawable.logo_principal),
                contentDescription = stringResource(R.string.app_name),
                modifier = Modifier.size(350.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))


            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                // Cards de la primera fila
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CardButton( // Pokédex
                        label = stringResource(R.string.pokedex),
                        onClick = { navController.navigate(AppScreens.Pokedex.route) },
                        icon = painterResource(id = R.drawable.pokedex)
                    )
                    CardButton( // Tareas
                        label = stringResource(R.string.tasks),
                        onClick = { navController.navigate(AppScreens.Tasks.route) },
                        icon = painterResource(id = R.drawable.about)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Cards de la segunda fila
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CardButton( // Ayuda
                        label = stringResource(R.string.help),
                        onClick = { navController.navigate(AppScreens.Ayuda.route) },
                        icon = painterResource(id = R.drawable.help)
                    )
                    CardButton( // Configuración
                        label = stringResource(R.string.settings),
                        onClick = { navController.navigate(AppScreens.Configuracion.route) },
                        icon = painterResource(id = R.drawable.settings)
                    )
                }
            }
        }

        // Botón de inicio/cierre de sesión
        TextButton(
            onClick = {
                if (authState is AuthState.Authenticated) {
                    authViewModel.signout()
                } else {
                    navController.navigate(AppScreens.Login.route)
                }
            },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Text(
                text = if (authState is AuthState.Authenticated) {
                    stringResource(R.string.logout) + " (${authViewModel.currentUserEmail})"
                } else {
                    stringResource(R.string.login)
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

// Función de las Card
@Composable
fun CardButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: Painter? = null,
    iconContentDescription: String? = null
) {
    ElevatedCard(
        onClick = onClick,
        modifier = modifier
            .width(150.dp)
            .height(60.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Icono
                if (icon != null) {
                    Image(
                        painter = icon,
                        contentDescription = iconContentDescription,
                        modifier = Modifier
                            .size(24.dp)
                            .padding(end = 8.dp)
                    )
                }
                // Texto
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
