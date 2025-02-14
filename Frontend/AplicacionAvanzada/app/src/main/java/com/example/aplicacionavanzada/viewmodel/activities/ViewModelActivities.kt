package com.example.aplicacionavanzada.viewmodel.activities

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacionavanzada.model.activities.ActivitiesRepository
import com.example.aplicacionavanzada.model.activities.ActivityResponse
import com.example.aplicacionavanzada.model.activities.ParticipationResponse
import com.example.aplicacionavanzada.viewmodel.authentication.AuthViewModel.Companion.ACCESS_TOKEN
import com.example.aplicacionavanzada.viewmodel.authentication.AuthViewModel.Companion.REFRESH_TOKEN
import com.example.aplicacionavanzada.viewmodel.authentication.AuthViewModel.Companion.USER_EMAIL
import com.example.aplicacionavanzada.viewmodel.authentication.AuthViewModel.Companion.USER_ID
import com.example.aplicacionavanzada.viewmodel.authentication.AuthViewModel.Companion.dataStoreAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ViewModelActivities(application: Application) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    private val context = application.applicationContext
    private val activitiesRepository = ActivitiesRepository()

    private val _activities = MutableStateFlow<List<ActivityResponse>>(emptyList())
    val activities: StateFlow<List<ActivityResponse>> = _activities

    private val _participations = MutableStateFlow<List<ParticipationResponse>>(emptyList())
    val participations: StateFlow<List<ParticipationResponse>> = _participations

    private val _accessToken = MutableStateFlow("")
    val accessToken: StateFlow<String> = _accessToken

    private val _refreshToken = MutableStateFlow("")
    val refreshToken: StateFlow<String> = _refreshToken

    private val _userId = MutableStateFlow<String?>(null)
    val userId: StateFlow<String?> = _userId

    private val _userEmail = MutableStateFlow<String?>(null)
    val userEmail: StateFlow<String?> = _userEmail

    init {
        getData()
    }

    // Cargar datos desde DataStore
    private fun getData() {
        viewModelScope.launch {
            val preferences = context.dataStoreAuth.data.first()

            _accessToken.value = preferences[ACCESS_TOKEN] ?: ""
            _refreshToken.value = preferences[REFRESH_TOKEN] ?: ""
            _userId.value = preferences[USER_ID] ?: ""
            _userEmail.value = preferences[USER_EMAIL] ?: ""

            Log.d("Activities", "Cargando datos desde DataStore:")
            Log.d("Activities", "Token de acceso: " + _accessToken.value)
            Log.d("Activities", "Token de refresco: " + _refreshToken.value)
            Log.d("Activities", "ID del usuario: " + _userId.value!!)
            Log.d("Activities", "Email del usuario: " + _userEmail.value!!)
            Log.d("Activities", "Fin de la carga de datos.")
        }
    }

    // Lista de actividades
    fun getAllActivities() {
        viewModelScope.launch {
            val token = _accessToken.value
            if (token.isNotEmpty()) {
                val activities = activitiesRepository.getAllActivities(token)
                _activities.value = activities
                Log.d("Activities", "Actividades: " +_activities.value)
            }
        }
    }

    // Lista de participaciones del usuario
    fun getUserActivities() {
        viewModelScope.launch {
            val token = _accessToken.value
            val userId = _userId.value
            if (token.isNotEmpty() && !userId.isNullOrEmpty()) {
                val participations = activitiesRepository.getAllParticipation(token, userId)
                _participations.value = participations
                Log.d("Activities", "Participaciones: " +_participations.value)
            }
        }
    }

    // Registra la participación del usuario en una actividad
    fun createParticipation(activityId: Long) {
        viewModelScope.launch {
            val token = _accessToken.value
            val userId = _userId.value
            if (token.isNotEmpty() && !userId.isNullOrEmpty()) {
                activitiesRepository.setParticipation(token, userId, activityId)
                getUserActivities()
            }
        }
    }

    // Elimina la participación del usuario en una actividad
    fun deleteParticipation(activityId: Long) {
        viewModelScope.launch {
            val token = _accessToken.value
            val userId = _userId.value
            if (token.isNotEmpty() && !userId.isNullOrEmpty()) {

                val participation = _participations.value.find {
                    it.activityId == activityId
                }

                participation?.let {
                    activitiesRepository.deleteParticipation(token, it.id)
                    getUserActivities()
                }
            }
        }
    }
}