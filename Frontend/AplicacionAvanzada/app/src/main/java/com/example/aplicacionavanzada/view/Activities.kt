package com.example.aplicacionavanzada.view

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.aplicacionavanzada.R
import com.example.aplicacionavanzada.model.AppScreens
import com.example.aplicacionavanzada.viewmodel.activities.ViewModelActivities
import com.example.aplicacionavanzada.viewmodel.authentication.AuthState
import com.example.aplicacionavanzada.viewmodel.authentication.AuthViewModel
import kotlinx.coroutines.launch

data class TabItem(
    val title: String,
    val icon: ImageVector
)

@Composable
fun Activities(
    viewModelActivities: ViewModelActivities,
    authViewModel: AuthViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val currentState by authViewModel.authState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Comprobar el estado de autenticación
    LaunchedEffect(Unit) {
        if (currentState !is AuthState.Authenticated) {
            navController.navigate(AppScreens.Login.route) {
                popUpTo("activities") {
                    inclusive = true
                }
            }

            Toast.makeText(
                context,
                context.getString(R.string.login_warning),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    // Datos de ViewModelActivities
    val accessToken by viewModelActivities.accessToken.collectAsState()
    val activitiesList by viewModelActivities.activities.collectAsState()
    val participationsList by viewModelActivities.participations.collectAsState()

    // Obtener las listas de actividades y participaciones
    LaunchedEffect(accessToken) {
        if (accessToken.isNotEmpty()) {
            viewModelActivities.getAllActivities()
            viewModelActivities.getUserActivities()
        }
    }

    // Pestañas
    val tabs = listOf(
        TabItem(
            title = stringResource(id = R.string.all_activities),
            icon = Icons.AutoMirrored.Filled.List
        ),
        TabItem(
            title = stringResource(id = R.string.my_activities),
            icon = Icons.Default.Person
        )
    )

    // Página seleccionada
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 2 })

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {

            // Pestañas
            TabRow(
                selectedTabIndex = pagerState.currentPage
            ) {
                tabs.forEachIndexed { index, tab ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = { Text(tab.title) },
                        icon = { Icon(tab.icon, contentDescription = tab.title) }
                    )
                }
            }

            // Contenido de cada pestaña
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> Text(text = "Pestaña 1")
                    1 -> Text(text = "Pestaña 2")
                }
            }
        }
    }
}