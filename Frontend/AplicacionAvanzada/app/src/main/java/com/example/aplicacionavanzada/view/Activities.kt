package com.example.aplicacionavanzada.view

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aplicacionavanzada.R
import com.example.aplicacionavanzada.model.AppScreens
import com.example.aplicacionavanzada.model.activities.ActivityResponse
import com.example.aplicacionavanzada.view.dialogs.ToastMessage
import com.example.aplicacionavanzada.viewmodel.activities.ViewModelActivities
import com.example.aplicacionavanzada.viewmodel.authentication.AuthState
import com.example.aplicacionavanzada.viewmodel.authentication.AuthViewModel
import kotlinx.coroutines.delay
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
        if (currentState is AuthState.Unauthenticated) {
            navController.navigate(AppScreens.Login.route) {
                popUpTo("activities") { inclusive = true }
            }
            Toast.makeText(
                context,
                context.getString(R.string.login_warning),
                Toast.LENGTH_LONG
            ).show()
        }

        if (currentState is AuthState.Error) {
            val messageId = ToastMessage.getStringResourceId((currentState as AuthState.Error).messageKey)
            val translatedMessage = context.getString(messageId)

            navController.navigate(AppScreens.Login.route) {
                popUpTo("activities") { inclusive = true }
            }
            Toast.makeText(
                context,
                translatedMessage,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    // Datos de ViewModelActivities
    val accessToken by viewModelActivities.accessToken.collectAsState()
    val activitiesList by viewModelActivities.activities.collectAsState()
    val participationsList by viewModelActivities.participations.collectAsState()

    // Estados de carga
    var activitiesLoading by remember { mutableStateOf(true) }
    var participationsLoading by remember { mutableStateOf(true) }

    // Obtener las listas de actividades y participaciones
    LaunchedEffect(accessToken) {
        if (accessToken.isEmpty()) {
            viewModelActivities.getData()
        } else if (accessToken.isNotEmpty()) {
            viewModelActivities.getAllActivities()
            viewModelActivities.getUserActivities()
            delay(1000)
            activitiesLoading = false
            participationsLoading = false
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
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { tabs.size })

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {

            // Pestañas
            TabRow(selectedTabIndex = pagerState.currentPage) {
                tabs.forEachIndexed { index, tab ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
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
                    0 -> {
                        // Pestaña de todas las actividades
                        if (activitiesLoading) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) { CircularProgressIndicator() }

                        } else {

                            if (activitiesList.isEmpty()) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) { Text(text = stringResource(id = R.string.no_activities)) }
                            } else {
                                ActivityList(
                                    activityList = activitiesList,
                                    viewModelActivities = viewModelActivities,
                                    snackbarHostState = snackbarHostState,
                                    isUserActivity = false
                                )
                            }
                        }
                    }

                    1 -> {
                        // Pestaña de actividades en las que está apuntado el usuario
                        if (participationsLoading) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) { CircularProgressIndicator() }

                        } else {

                            if (participationsList.isEmpty()) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) { Text(text = stringResource(id = R.string.no_participations)) }
                            } else {
                                ActivityList(
                                    activityList = participationsList,
                                    viewModelActivities = viewModelActivities,
                                    snackbarHostState = snackbarHostState,
                                    isUserActivity = true
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// Lista de actividades
@Composable
fun ActivityList(
    activityList: List<ActivityResponse>,
    viewModelActivities: ViewModelActivities,
    snackbarHostState: SnackbarHostState,
    isUserActivity: Boolean
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        items(
            items = activityList,
            key = { activity -> activity.id }
        ) { activity ->
            ActivityItem(
                activity = activity,
                viewModelActivities = viewModelActivities,
                snackbarHostState = snackbarHostState,
                isUserActivity = isUserActivity
            )
        }
    }
}

// Actividades
@Composable
fun ActivityItem(
    activity: ActivityResponse,
    viewModelActivities: ViewModelActivities,
    snackbarHostState: SnackbarHostState,
    isUserActivity: Boolean
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var visible by remember { mutableStateOf(true) }

    AnimatedVisibility(
        visible = visible,
        exit = slideOutHorizontally() + fadeOut()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            shape = MaterialTheme.shapes.large
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Datos de la actividad
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = activity.name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = activity.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Inicio: ${activity.startDate}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Fin: ${activity.endDate}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Lugar: ${activity.place}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Botón para apuntarse
                if (!isUserActivity) {
                    IconButton(onClick = {
                        scope.launch {
                            val response =
                                viewModelActivities.createParticipation(activity.id.toLong())
                            val message = if (response.id.isNotEmpty()) {
                                context.getString(R.string.signup_success_activity)
                            } else {
                                context.getString(R.string.signup_error_activity)
                            }
                            snackbarHostState.showSnackbar(
                                message = message,
                                duration = SnackbarDuration.Long
                            )
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(id = R.string.sign_up_activity)
                        )
                    }

                // Botón para eliminarse
                } else {
                    IconButton(onClick = {
                        visible = false
                        scope.launch {
                            delay(400)
                            val response = viewModelActivities.deleteParticipation(activity.id)
                            val message = if (response) {
                                context.getString(R.string.unsubscribe_success_activity)
                            } else {
                                context.getString(R.string.unsubscribe_error_activity)
                            }
                            snackbarHostState.showSnackbar(
                                message = message,
                                duration = SnackbarDuration.Long
                            )
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(id = R.string.unsubscribe_activity)
                        )
                    }
                }
            }
        }
    }
}