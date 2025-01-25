package com.example.aplicacionavanzada

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aplicacionavanzada.view.theme.AplicacionAvanzadaTheme
import com.example.aplicacionavanzada.view.navigationdrawer.NavigationDrawer
import com.example.aplicacionavanzada.viewmodel.authentication.AuthViewModel
import com.example.aplicacionavanzada.viewmodel.tasks.TasksViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AplicacionAvanzadaTheme {
                val authViewModel: AuthViewModel by viewModels()
                val tasksViewModel: TasksViewModel = viewModel(factory = TasksViewModel.Factory)
                NavigationDrawer(authViewModel = authViewModel, viewModel = tasksViewModel)
            }
        }
    }
}