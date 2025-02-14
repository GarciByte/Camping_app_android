package com.example.aplicacionavanzada.view.navigationdrawer

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aplicacionavanzada.R
import com.example.aplicacionavanzada.model.AppScreens
import com.example.aplicacionavanzada.view.AcercaDe
import com.example.aplicacionavanzada.view.Activities
import com.example.aplicacionavanzada.view.Ayuda
import com.example.aplicacionavanzada.view.Configuracion
import com.example.aplicacionavanzada.view.Pokedex
import com.example.aplicacionavanzada.view.Principal
import com.example.aplicacionavanzada.view.Tasks
import com.example.aplicacionavanzada.view.authentication.Login
import com.example.aplicacionavanzada.view.authentication.Signup
import com.example.aplicacionavanzada.view.dialogs.GeneralAlertDialog
import com.example.aplicacionavanzada.viewmodel.activities.ViewModelActivities
import com.example.aplicacionavanzada.viewmodel.authentication.AuthViewModel
import com.example.aplicacionavanzada.viewmodel.tasks.TasksViewModel
import kotlinx.coroutines.launch

data class NavigationItem(
    val title: String,
    val onClick: () -> Unit,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawer(authViewModel: AuthViewModel, viewModel: TasksViewModel, viewModelActivities: ViewModelActivities) {

    val pendingTasks by viewModel.getPendingTaskCount().collectAsState(initial = 0)
    val navController = rememberNavController()
    val context = LocalContext.current
    val activity = context as? Activity
    var showDialog by rememberSaveable { mutableStateOf(false) }

    val items = listOf(
        NavigationItem(
            title = stringResource(R.string.home),
            onClick = { navController.navigate(AppScreens.Principal.route) },
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home
        ),
        NavigationItem(
            title = stringResource(R.string.tasks),
            onClick = { navController.navigate(AppScreens.Tasks.route) },
            selectedIcon = Icons.Filled.Check,
            unselectedIcon = Icons.Outlined.Check,
            badgeCount = pendingTasks
        ),
        NavigationItem(
            title = stringResource(R.string.activities),
            onClick = { navController.navigate(AppScreens.Activities.route) },
            selectedIcon = Icons.AutoMirrored.Filled.Send,
            unselectedIcon = Icons.AutoMirrored.Outlined.Send
        ),
        NavigationItem(
            title = stringResource(R.string.pokedex),
            onClick = { navController.navigate(AppScreens.Pokedex.route) },
            selectedIcon = Icons.AutoMirrored.Filled.List,
            unselectedIcon = Icons.AutoMirrored.Outlined.List
        ),
        NavigationItem(
            title = stringResource(R.string.settings),
            onClick = { navController.navigate(AppScreens.Configuracion.route) },
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings
        ),
        NavigationItem(
            title = stringResource(R.string.help),
            onClick = { navController.navigate(AppScreens.Ayuda.route) },
            selectedIcon = Icons.Filled.Info,
            unselectedIcon = Icons.Outlined.Info
        ),
        NavigationItem(
            title = stringResource(R.string.about),
            onClick = { navController.navigate(AppScreens.AcercaDe.route) },
            selectedIcon = Icons.Filled.Place,
            unselectedIcon = Icons.Outlined.Place
        ),
        NavigationItem(
            title = stringResource(R.string.exit),
            onClick = { showDialog = true },
            selectedIcon = Icons.Filled.Close,
            unselectedIcon = Icons.Outlined.Close
        )
    )

    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.app_name),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                items.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = item.title,
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        },
                        selected = index == selectedItemIndex,
                        onClick = {
                            selectedItemIndex = index
                            item.onClick()
                            scope.launch {
                                drawerState.close()
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = if (index == selectedItemIndex) {
                                    item.selectedIcon
                                } else item.unselectedIcon,
                                contentDescription = item.title
                            )
                        },
                        badge = {
                            item.badgeCount?.let {
                                if (item.badgeCount > 0) {
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                color = MaterialTheme.colorScheme.secondary,
                                                shape = MaterialTheme.shapes.large
                                            )
                                            .padding(4.dp)
                                    ) {
                                        Text(
                                            text = item.badgeCount.toString(),
                                            style = MaterialTheme.typography.titleSmall,
                                            color = MaterialTheme.colorScheme.onSecondary,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .padding(NavigationDrawerItemDefaults.ItemPadding)
                            .background(
                                if (index == selectedItemIndex) MaterialTheme.colorScheme.secondaryContainer
                                else Color.Transparent,
                                shape = MaterialTheme.shapes.large
                            )
                    )
                }

            }
        },
        gesturesEnabled = true
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.app_name),
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onSurface,
                        navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                        actionIconContentColor = MaterialTheme.colorScheme.onSurface,
                    ),
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = stringResource(R.string.menu)
                            )
                        }
                    }
                )
            }
        ) {
            Box(modifier = Modifier.padding(it)) {
                NavHost(
                    navController = navController,
                    startDestination = AppScreens.Principal.route
                ) {
                    composable(AppScreens.Principal.route) {
                        Principal(authViewModel = authViewModel, navController = navController)
                    }
                    composable(AppScreens.Configuracion.route) {
                        Configuracion(navController = navController)
                    }
                    composable(AppScreens.Ayuda.route) {
                        Ayuda(navController = navController)
                    }
                    composable(AppScreens.AcercaDe.route) {
                        AcercaDe(navController = navController)
                    }
                    composable(AppScreens.Pokedex.route) {
                        Pokedex(viewModel(), navController = navController)
                    }
                    composable(AppScreens.Login.route) {
                        Login(authViewModel = authViewModel, navController = navController)
                    }
                    composable(AppScreens.Signup.route) {
                        Signup(authViewModel = authViewModel, navController = navController)
                    }
                    composable(AppScreens.Tasks.route) {
                        Tasks(navController = navController)
                    }
                    composable(AppScreens.Activities.route) {
                        Activities(
                            viewModelActivities = viewModelActivities,
                            authViewModel = authViewModel,
                            navController = navController
                        )
                    }
                }
            }
        }
    }

    // Diálogo de confirmación para salir
    if (showDialog) {
        GeneralAlertDialog(
            title = stringResource(R.string.exit_confirmation),
            message = stringResource(R.string.exit_confirmation_message),
            confirmText = stringResource(R.string.confirm),
            dismissText = stringResource(R.string.cancel),
            icon = Icons.Default.Info,
            onConfirm = {
                activity?.finish() // Cerrar la aplicación
            },
            onDismiss = { showDialog = false }
        )
    }
}