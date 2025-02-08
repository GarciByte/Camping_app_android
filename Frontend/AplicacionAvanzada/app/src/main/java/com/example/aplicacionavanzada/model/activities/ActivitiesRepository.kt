package com.example.aplicacionavanzada.model.activities

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ActivitiesRepository {
    private val activitiesClient = ActivitiesRetrofitInstance.activitiesClient

    // Obtener todas las actividades
    suspend fun getAllActivities(accessToken: String): List<ActivityResponse> {
        val bearerToken = "Bearer $accessToken"
        return withContext(Dispatchers.IO) {
            val response = activitiesClient.getActivities(bearerToken)
            response.body() ?: emptyList()
        }
    }

    // Obtener todas las actividades en las que participa un usuario
    suspend fun getAllParticipation(accessToken: String, userId: String): List<ParticipationResponse> {
        val bearerToken = "Bearer $accessToken"
        return withContext(Dispatchers.IO) {
            val response = activitiesClient.getUserParticipation(bearerToken, userId)
            response.body() ?: emptyList()
        }
    }

    // Registrar una nueva participación
    suspend fun setParticipation(accessToken: String, userId: String, activityId: Long): ParticipationResponse {
        val bearerToken = "Bearer $accessToken"
        return withContext(Dispatchers.IO) {
            val response = activitiesClient.setUserParticipation(bearerToken, ParticipationRequest(userId, activityId))
            ParticipationResponse(
                response.body()?.id ?: "",
                response.body()?.userId ?: "",
                response.body()?.activityId ?: 0
            )
        }
    }

    // Eliminar una participación
    suspend fun deleteParticipation(accessToken: String, participationId: String) {
        val bearerToken = "Bearer $accessToken"
        return withContext(Dispatchers.IO) {
            activitiesClient.deleteUserParticipation(bearerToken, participationId)
        }
    }
}