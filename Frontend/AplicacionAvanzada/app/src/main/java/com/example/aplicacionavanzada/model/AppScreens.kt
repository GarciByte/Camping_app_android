package com.example.aplicacionavanzada.model

sealed class AppScreens(val route: String) {
    data object Principal : AppScreens("principal")
    data object Configuracion : AppScreens("configuracion")
    data object Ayuda : AppScreens("ayuda")
    data object AcercaDe : AppScreens("acercaDe")
    data object Pokedex : AppScreens("pokedex")
    data object Login : AppScreens("login")
    data object Signup : AppScreens("signup")
    data object Tasks : AppScreens("tasks")
}