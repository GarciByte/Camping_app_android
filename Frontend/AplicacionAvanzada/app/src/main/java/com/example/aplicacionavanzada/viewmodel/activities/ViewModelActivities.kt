package com.example.aplicacionavanzada.viewmodel.activities

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacionavanzada.model.activities.ActivitiesRepository
import com.example.aplicacionavanzada.model.activities.ActivityResponse
import com.example.aplicacionavanzada.model.activities.ParticipationResponse
import com.example.aplicacionavanzada.viewmodel.authentication.AuthViewModel.Companion.ACCESS_TOKEN
import com.example.aplicacionavanzada.viewmodel.authentication.AuthViewModel.Companion.USER_ID
import com.example.aplicacionavanzada.viewmodel.authentication.AuthViewModel.Companion.dataStoreAuth
import kotlinx.coroutines.delay
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

    private val _participations = MutableStateFlow<List<ActivityResponse>>(emptyList())
    val participations: StateFlow<List<ActivityResponse>> = _participations

    private val _accessToken = MutableStateFlow("")
    val accessToken: StateFlow<String> = _accessToken

    private val _userId = MutableStateFlow<String?>(null)
    val userId: StateFlow<String?> = _userId

    init {
        getData()
    }

    // Cargar datos desde DataStore
    fun getData() {
        viewModelScope.launch {
            try {
                val preferences = context.dataStoreAuth.data.first()

                _accessToken.value = preferences[ACCESS_TOKEN] ?: ""
                _userId.value = preferences[USER_ID] ?: ""

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Lista de actividades
    fun getAllActivities() {
        viewModelScope.launch {
            try {
                val token = _accessToken.value
                if (token.isNotEmpty()) {
                    val activities = activitiesRepository.getAllActivities(token)
                    _activities.value = activities
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Lista de participaciones del usuario
    fun getUserActivities() {
        viewModelScope.launch {
            try {
                delay(1500)
                val token = _accessToken.value
                val userId = _userId.value
                if (token.isNotEmpty() && !userId.isNullOrEmpty()) {
                    val participations = activitiesRepository.getAllParticipation(token, userId)
                    _participations.value = participations
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Registra la participación del usuario en una actividad
    suspend fun createParticipation(activityId: Long): ParticipationResponse {
        try {
            val token = _accessToken.value
            val userId = _userId.value
            return if (token.isNotEmpty() && !userId.isNullOrEmpty()) {
                val participationResponse =
                    activitiesRepository.setParticipation(token, userId, activityId)
                getUserActivities()
                participationResponse
            } else {
                ParticipationResponse("", "", 0)
            }

        } catch (e: Exception) {
            e.printStackTrace()
            return ParticipationResponse("", "", 0)
        }
    }

    // Elimina la participación del usuario en una actividad
    suspend fun deleteParticipation(activityId: String): Boolean {
        try {
            val token = _accessToken.value
            val userId = _userId.value
            if (token.isNotEmpty() && !userId.isNullOrEmpty()) {
                val response = activitiesRepository.deleteParticipation(token, activityId, userId)
                if (response) {
                    getUserActivities()
                    return true
                }
            }
            return false

        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
}