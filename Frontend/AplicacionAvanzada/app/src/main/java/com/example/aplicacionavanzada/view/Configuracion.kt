package com.example.aplicacionavanzada.view

import android.widget.Toast
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
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aplicacionavanzada.R
import com.example.aplicacionavanzada.model.ConfiguracionDataStore
import com.example.aplicacionavanzada.model.AppScreens
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun Configuracion(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = ConfiguracionDataStore(context)

    // Opciones del menú desplegable
    val landscapeOptions = getLandscapeOptions()

    // Variables de estado
    var notificationsEnabled by rememberSaveable { mutableStateOf(false) }
    var darkModeEnabled by rememberSaveable { mutableStateOf(false) }
    var tentRentalEnabled by rememberSaveable { mutableStateOf(false) }
    var wifiAccessEnabled by rememberSaveable { mutableStateOf(false) }
    var breakfastEnabled by rememberSaveable { mutableStateOf(false) }
    var selectedLandscape by rememberSaveable { mutableStateOf(landscapeOptions[0]) }
    var landscapeMenu by rememberSaveable { mutableStateOf(false) }

    // Cargar los valores actuales desde DataStore
    LaunchedEffect(Unit) {
        notificationsEnabled = dataStore.notificationsFlow.first()
        darkModeEnabled = dataStore.darkModeFlow.first()
        tentRentalEnabled = dataStore.tentRentalFlow.first()
        wifiAccessEnabled = dataStore.wifiAccessFlow.first()
        breakfastEnabled = dataStore.breakfastFlow.first()
        selectedLandscape = dataStore.landscapeTypeFlow.first()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título
        Text(
            text = stringResource(R.string.settings),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineMedium,
        )

        // Imagen
        Image(
            painter = painterResource(id = R.drawable.configuracion),
            contentDescription = stringResource(R.string.settings_image_desc),
            modifier = Modifier.size(160.dp)
        )

        // Switch de notificaciones
        SwitchItem(
            label = stringResource(R.string.notifications),
            isChecked = notificationsEnabled,
            onCheckedChange = { notificationsEnabled = it }
        )

        // Switch del modo oscuro
        SwitchItem(
            label = stringResource(R.string.dark_mode),
            isChecked = darkModeEnabled,
            onCheckedChange = { darkModeEnabled = it }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Servicios adicionales
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título
            Text(
                text = stringResource(R.string.additional_services),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )

            // Checkbox del alquiler de tienda
            CheckboxItem(
                label = stringResource(R.string.tent_rental),
                isChecked = tentRentalEnabled,
                onCheckedChange = { tentRentalEnabled = it }
            )

            // Checkbox del acceso wifi
            CheckboxItem(
                label = stringResource(R.string.wifi_access),
                isChecked = wifiAccessEnabled,
                onCheckedChange = { wifiAccessEnabled = it }
            )

            // Checkbox del desayuno
            CheckboxItem(
                label = stringResource(R.string.breakfast),
                isChecked = breakfastEnabled,
                onCheckedChange = { breakfastEnabled = it }
            )
        }

        // Menú del tipo de paisaje
        LandscapeDropdown(
            selectedLandscape = selectedLandscape,
            onLandscapeSelected = { landscape -> selectedLandscape = landscape },
            expanded = landscapeMenu,
            onExpandedChange = { landscapeMenu = it },
            landscapeOptions = landscapeOptions
        )

        Spacer(modifier = Modifier.height(2.dp))

        // Botón de guardar
        Button(
            onClick = {
                if (tentRentalEnabled && breakfastEnabled) {
                    // Mensaje largo
                    Toast.makeText(
                        context,
                        context.getString(R.string.long_toast_message),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    // Guardar datos
                    scope.launch {
                        dataStore.saveNotifications(notificationsEnabled)
                        dataStore.saveDarkMode(darkModeEnabled)
                        dataStore.saveTentRental(tentRentalEnabled)
                        dataStore.saveWifiAccess(wifiAccessEnabled)
                        dataStore.saveBreakfast(breakfastEnabled)
                        dataStore.saveLandscapeType(selectedLandscape)
                    }

                    // Mensaje corto
                    Toast.makeText(
                        context,
                        context.getString(R.string.short_toast_message),
                        Toast.LENGTH_SHORT
                    ).show()

                    // Volver a la pantalla principal
                    navController.navigate(AppScreens.Principal.route)
                }
            }
        ) {
            Text(text = stringResource(R.string.save_preferences))
        }
    }
}

// Función de los Switch
@Composable
fun SwitchItem(label: String, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // Nombre
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.weight(1f)
        )
        // Switch
        Switch(checked = isChecked, onCheckedChange = onCheckedChange)
    }
}

// Función de los Checkbox
@Composable
fun CheckboxItem(label: String, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Checkbox
        Checkbox(checked = isChecked, onCheckedChange = onCheckedChange)

        // Nombre
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

// Función con las opciones del menú desplegable
@Composable
fun getLandscapeOptions(): List<String> = listOf(
    stringResource(R.string.mountain),
    stringResource(R.string.forest),
    stringResource(R.string.beach)
)

// Función del Dropdownmenu
@Composable
fun LandscapeDropdown(
    selectedLandscape: String,
    onLandscapeSelected: (String) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    landscapeOptions: List<String>
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Titulo
        Text(
            text = stringResource(R.string.landscape_type),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box {
            // Botón que muestra el menú desplegable
            Button(
                onClick = { onExpandedChange(true) },
                shape = MaterialTheme.shapes.extraSmall
            ) {
                Text(
                    text = selectedLandscape,
                    modifier = Modifier.width(80.dp),
                    textAlign = TextAlign.Center
                )
            }

            // Menú desplegable
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) } // Cerrar el menú
            ) {
                // Elementos del menú
                landscapeOptions.forEach { landscape ->
                    DropdownMenuItem(
                        text = { Text(text = landscape) },
                        onClick = {
                            onLandscapeSelected(landscape)
                            onExpandedChange(false) // Cerrar el menú
                        }
                    )
                }
            }
        }
    }
}