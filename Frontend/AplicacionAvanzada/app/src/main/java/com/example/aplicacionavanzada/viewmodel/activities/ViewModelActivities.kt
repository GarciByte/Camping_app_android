package com.example.aplicacionavanzada.viewmodel.activities

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacionavanzada.model.activities.ActivitiesRepository
import com.example.aplicacionavanzada.model.activities.ActivityResponse
import com.example.aplicacionavanzada.model.activities.ParticipationResponse
import com.example.aplicacionavanzada.viewmodel.authentication.AuthViewModel
import com.example.aplicacionavanzada.viewmodel.authentication.AuthViewModel.Companion.dataStoreAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ViewModelActivities(application: Application) : AndroidViewModel(application) {

    private val activitiesRepository = ActivitiesRepository()

    private val _activitiesFlow = MutableStateFlow<List<ActivityResponse>>(emptyList())
    val activitiesFlow: StateFlow<List<ActivityResponse>> get() = _activitiesFlow

    private val _userActivitiesFlow = MutableStateFlow<List<ParticipationResponse>>(emptyList())
    val userActivitiesFlow: StateFlow<List<ParticipationResponse>> get() = _userActivitiesFlow

    private val _accessToken = MutableStateFlow("")
    val accessToken: StateFlow<String> get() = _accessToken

    private val _refreshToken = MutableStateFlow("")
    val refreshToken: StateFlow<String> get() = _refreshToken

    private val _userId = MutableStateFlow("")
    val userId: StateFlow<String> get() = _userId

    private val _userEmail = MutableStateFlow("")
    val userEmail: StateFlow<String> get() = _userEmail

    init {
        loadCredentials()
    }

    //  Carga las credenciales desde DataStore.
    private fun loadCredentials() {
        viewModelScope.launch(Dispatchers.IO) {
            val preferences = getApplication<Application>()
                .applicationContext
                .dataStoreAuth
                .data
                .first()

            _accessToken.value = preferences[AuthViewModel.ACCESS_TOKEN] ?: ""
            _refreshToken.value = preferences[AuthViewModel.REFRESH_TOKEN] ?: ""
            _userId.value = preferences[AuthViewModel.USER_ID] ?: ""
            _userEmail.value = preferences[AuthViewModel.USER_EMAIL] ?: ""
        }
    }

    // Lista de actividades
    fun getAllActivities() {
        viewModelScope.launch(Dispatchers.IO) {
            val token = _accessToken.value
            if (token.isNotEmpty()) {
                val activities = activitiesRepository.getAllActivities(token)
                _activitiesFlow.value = activities
            }
        }
    }

    // Lista de participaciones del usuario
    fun getUserActivities() {
        viewModelScope.launch(Dispatchers.IO) {
            val token = _accessToken.value
            val user = _userId.value
            if (token.isNotEmpty() && user.isNotEmpty()) {
                val participations = activitiesRepository.getAllParticipation(token, user)
                _userActivitiesFlow.value = participations
            }
        }
    }


    // Registra la participación del usuario en una actividad
    fun createParticipation(activityId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val token = _accessToken.value
            val user = _userId.value
            if (token.isNotEmpty() && user.isNotEmpty()) {
                val activityIdLong = activityId.toLongOrNull()
                activityIdLong?.let {
                    activitiesRepository.setParticipation(token, user, it)
                    getUserActivities()
                }
            }
        }
    }

    // Elimina la participación del usuario en una actividad
    fun deleteParticipation(activityId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val token = _accessToken.value
            val user = _userId.value
            if (token.isNotEmpty() && user.isNotEmpty()) {
                val participation = _userActivitiesFlow.value.find {
                    it.activityId.toString() == activityId
                }
                participation?.let {
                    activitiesRepository.deleteParticipation(token, it.id)
                    getUserActivities()
                }
            }
        }
    }
}